package com.marahoney.tasque.capture

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.marahoney.tasque.R
import com.marahoney.tasque.ui.splash.SplashActivity
import com.marahoney.tasque.util.OnSwipeTouchListener
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.sdk27.coroutines.onTouch
import java.util.*


class MyService : Service() {

    val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    var view: View? = null
    var screenCapture: ScreenCapture? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        screenCapture = ScreenCapture(this)
        val resultCode = intent?.getIntExtra("resultCode", 0) ?: 0
        val data = intent
        screenCapture?.onActivityResult(resultCode, data)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
        }
        val dp = makeDP(this, 1f)
        val params = WindowManager.LayoutParams(
                (dp * 10).toInt(),
                (dp * 50).toInt(),
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT).apply {
            verticalMargin = 0.2f
            gravity = Gravity.TOP or Gravity.END
        }

        val view = View(this).apply {
            backgroundResource = R.drawable.overlay
        }

        windowManager.addView(view, params)

        val swipeTouchListener = object : OnSwipeTouchListener(this@MyService) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                screenCapture?.capture {
                    var topPackageName: String = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        val mUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                        val time = System.currentTimeMillis()
                        // We get usage stats for the last 10 seconds
                        val stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
                        // Sort the stats by the last time used
                        if (stats != null) {
                            val mySortedMap = TreeMap<Long, UsageStats>()
                            for (usageStats in stats) {
                                mySortedMap[usageStats.lastTimeUsed] = usageStats
                            }
                            if (!mySortedMap.isEmpty()) {
                                topPackageName = mySortedMap.get(mySortedMap.lastKey())?.packageName
                                        ?: ""
                            }
                        }
                    }
                    Log.e("asdf", "yee ${topPackageName}")
                    val intent = Intent(applicationContext, SplashActivity::class.java).apply {
                        putExtra("mode", SplashActivity.MODE_CREATE_FORM)
                        putExtra(SplashActivity.KEY_FILE_PATH, it)
                        putExtra(SplashActivity.KEY_PACKAGE_NAME, topPackageName)
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    applicationContext.startActivity(intent)
                }
            }
        }

        view.onTouch { v, event ->
            try {
                swipeTouchListener.onTouch(v, event ?: return@onTouch)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return START_STICKY
    }

    fun makeDP(context: Context, dp: Float): Float = context.displayMetrics.density * dp

    private fun startForegroundService() {
        val notificationIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val remoteViews = RemoteViews(packageName, R.layout.activity_splash)

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "test_channel"
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    "캡쳐",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        builder.setSmallIcon(com.marahoney.tasque.R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)

        startForeground(1, builder.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}
