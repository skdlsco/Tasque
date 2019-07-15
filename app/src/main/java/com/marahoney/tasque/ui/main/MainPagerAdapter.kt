package com.marahoney.tasque.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.marahoney.tasque.ui.f_category.CategoryFragment
import com.marahoney.tasque.ui.f_form.FormFragment
import com.marahoney.tasque.ui.f_setting.SettingFragment
import com.marahoney.tasque.ui.f_share.ShareFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val pageTitles = arrayOf("카테고리", "폼", "쉐어", "설정")

    override fun getPageTitle(position: Int): CharSequence = pageTitles[position]

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> CategoryFragment.newInstance()
                1 -> FormFragment.newInstance()
                2 -> ShareFragment.newInstance()
                3 -> SettingFragment.newInstance()
                else -> CategoryFragment.newInstance()
            }

    override fun getCount(): Int = 4
}