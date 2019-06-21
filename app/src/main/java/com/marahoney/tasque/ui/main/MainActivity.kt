package com.marahoney.tasque.ui.main

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.marahoney.tasque.R
import com.marahoney.tasque.capture.MyService
import com.marahoney.tasque.capture.ScreenCapture
import com.marahoney.tasque.databinding.ActivityMainBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.appcompat.v7.coroutines.onClose
import org.jetbrains.anko.appcompat.v7.coroutines.onSearchClick
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int = R.layout.activity_main

    private val useCase by lazy { MainUseCase(this) }
    private val viewModel by viewModel<MainViewModel> { parametersOf(useCase) }

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        initToolbar()
        initViewPager()
        initTabLayout()

        if (!hasUsageStatsPermission())
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
        }

        startActivityForResult((getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent(),
                ScreenCapture.REQUEST_MEDIA_PROJECTION)
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOpsManager = applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), applicationContext.packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun initViewPager() {
        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
    }

    private fun initTabLayout() {
        tabLayout.run {
            addTab(newTab())
            addTab(newTab())
            addTab(newTab())
            addTab(newTab())
            setupWithViewPager(viewPager)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("asdfs", "${requestCode}")
        Log.e("asdfs", "${resultCode}")
        try {
            startService(Intent(this, MyService::class.java).apply {
                putExtra("resultCode", resultCode)
                putExtras(data)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_nav_logo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_category, menu)
        (menu?.findItem(R.id.search)?.actionView as SearchView)?.run {
            onSearchClick {
                menu.findItem(R.id.add).isVisible = false
            }
            onClose {
                menu.findItem(R.id.add).isVisible = true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_OVERLAY_PERMISSION = 1000
    }
}
