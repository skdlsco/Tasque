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
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.ui.splash.SplashActivity
import com.marahoney.tasque.util.OnSwipeTouchListener
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*


class MyService : Service() {

    val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    var view: View? = null
    var screenCapture: ScreenCapture? = null
    val pref by lazy { getSharedPreferences("webInfo", Context.MODE_PRIVATE) }

    val dataRepository by inject<DataRepository>()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    val observer = ShowObserver()

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        dataRepository.isShow.observeForever(observer)
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

        observer.view = view
        windowManager.addView(view, params)

        val swipeTouchListener = object : OnSwipeTouchListener(this@MyService) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                val topPackageName: String = getTopPackageName()
                if (topPackageName == "com.android.chrome" || topPackageName == "com.sec.android.app.sbrowser")
                    fromWeb(topPackageName)
                else
                    fromApp(topPackageName)
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

    fun fromWeb(packageName: String) {
        Log.e("asdf", "fromWeb -> ${packageName}")
        val webUrl = pref.getString("web_url", "")!!
        Log.e("Asdf", "success ${webUrl}")
        screenCapture?.capture {
            val intent = Intent(applicationContext, SplashActivity::class.java).apply {
                putExtra("mode", SplashActivity.MODE_CREATE_FORM)
                putExtra(SplashActivity.KEY_FILE_PATH, it)
                putExtra(SplashActivity.KEY_PACKAGE_NAME, packageName)
                putExtra(SplashActivity.KEY_WEB_LINK, webUrl)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(it))))
            applicationContext.startActivity(intent)
        }
    }

    fun fromApp(packageName: String) {
        screenCapture?.capture {
            val intent = Intent(applicationContext, SplashActivity::class.java).apply {
                putExtra("mode", SplashActivity.MODE_CREATE_FORM)
                putExtra(SplashActivity.KEY_FILE_PATH, it)
                putExtra(SplashActivity.KEY_PACKAGE_NAME, packageName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(it))))
            applicationContext.startActivity(intent)
        }
    }

    fun getTopPackageName(): String {
        var topPackageName: String = ""
        val time = System.currentTimeMillis()
        val mUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
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
        return topPackageName
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
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)

        startForeground(1, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        dataRepository.isShow.removeObserver(observer)
        stopForeground(true)
    }

    class ShowObserver : Observer<Boolean> {
        var view: View? = null

        override fun onChanged(t: Boolean?) {
            if (t == null || view == null)
                return

            view?.visibility = if (t) View.VISIBLE else View.GONE
        }
    }
}
