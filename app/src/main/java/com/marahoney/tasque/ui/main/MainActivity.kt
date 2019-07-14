package com.marahoney.tasque.ui.main

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.marahoney.tasque.R
import com.marahoney.tasque.capture.MyService
import com.marahoney.tasque.capture.ScreenCapture
import com.marahoney.tasque.databinding.ActivityMainBinding
import com.marahoney.tasque.ui.base.BaseActivity
import com.marahoney.tasque.ui.category_edit.CategoryEditActivity
import com.marahoney.tasque.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.ref.WeakReference


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int = R.layout.activity_main

    private val useCase by lazy { MainUseCase(this) }
    private val viewModel by viewModel<MainViewModel> { parametersOf(useCase) }

    private var menu: Menu? = null

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            menu?.findItem(R.id.add)?.isVisible = position == 0
        }
    }

    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {

        var tabLayout: WeakReference<TabLayout?> = WeakReference(null)
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            if (tabLayout == null || tab == null)
                return
            val tabLayout = (tabLayout.get()?.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
            val tabTextView = tabLayout.getChildAt(1) as TextView
            tabTextView.setTypeface(tabTextView.typeface, Typeface.NORMAL)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tabLayout == null || tab == null)
                return

            val tabLayout = (tabLayout.get()?.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
            val tabTextView = tabLayout.getChildAt(1) as TextView
            tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)
        }
    }

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
        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.offscreenPageLimit = 4
    }

    private fun initTabLayout() {
        tabLayout.run {
            addTab(newTab())
            addTab(newTab())
            addTab(newTab())
            addTab(newTab())
            setupWithViewPager(viewPager)
        }
        tabSelectedListener.tabLayout = WeakReference(tabLayout)
        tabLayout.addOnTabSelectedListener(tabSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
        this.menu = menu
        menuInflater.inflate(R.menu.main_category, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                startActivity<SearchActivity>()
            }

            R.id.add -> {
                startActivity<CategoryEditActivity>(CategoryEditActivity.KEY_MODE to CategoryEditActivity.MODE_CREATE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.removeOnPageChangeListener(onPageChangeListener)
    }

    companion object {
        const val REQUEST_OVERLAY_PERMISSION = 1000
    }
}
