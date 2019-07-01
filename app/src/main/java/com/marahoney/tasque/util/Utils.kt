package com.marahoney.tasque.util

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import org.jetbrains.anko.displayMetrics
import java.util.*

object Utils {

    fun makeDP(context: Context, dp: Float): Float = context.displayMetrics.density * dp

    fun getAppNameFromPkgName(context: Context, packageName: String): String {

        val mUsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
        val time = System.currentTimeMillis()
        // We get usage stats for the last 10 seconds
        val stats =
                mUsageStatsManager!!.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60 * 60 * 24, time)
        // Sort the stats by the last time used
        if (stats != null) {
            val mySortedMap = TreeMap<Long, UsageStats>()
            for (usageStats in stats) {
                mySortedMap[usageStats.lastTimeUsed] = usageStats
            }
            if (!mySortedMap.isEmpty()) {
                return mySortedMap[mySortedMap.lastKey()]?.packageName ?: ""
            }
        }
        return ""
    }


}