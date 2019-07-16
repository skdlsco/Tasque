package com.marahoney.tasque.capture

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.net.Uri
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo


class MyAccessibilityService : AccessibilityService() {
    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        val eventType = AccessibilityEvent.eventTypeToString(event!!.eventType)
        when (event?.packageName) {
            "com.android.chrome" -> searchChromeUrl(event.source)
            "com.sec.android.app.sbrowser" -> searchSBrowserUrl(event.source)
        }
    }

    var sbrowserUrl = ""
    var sbrowserLastUrl = ""

    private fun searchSBrowserUrl(nodeInfo: AccessibilityNodeInfo?) {
        if (nodeInfo == null) {
            return
        }
        val unvisited = ArrayList<AccessibilityNodeInfo>()
        unvisited.add(nodeInfo)

        while (unvisited.isNotEmpty()) {
            val node = unvisited.removeAt(0) ?: continue
            if ("android.widget.EditText" == node.className.toString() && node.viewIdResourceName == "com.sec.android.app.sbrowser:id/location_bar_edit_text") {
                if (sbrowserLastUrl == node.text?.toString()) {
                    // 입력중이 아닐 때 (실제로 사이트가 변경, 새로고침 된경우)
                    if (node.text.toString().contains("/")
                            || getHost(node.text.toString()) != getHost(sbrowserUrl)) {
                        sbrowserUrl = node.text.toString()
                        saveUrl(getHost(sbrowserUrl))

                    }
                }
                sbrowserLastUrl = node.text?.toString() ?: ""
            }
            for (i in 0 until node.childCount) {
                unvisited.add(node.getChild(i))
            }
        }
    }

    fun getHost(url: String): String? {
        return if (!url.startsWith("http"))
            Uri.parse("http://$url").host
        else Uri.parse(url).host
    }

    private fun searchChromeUrl(nodeInfo: AccessibilityNodeInfo?) {
        if (nodeInfo == null) {
            return
        }
        val unvisited = ArrayList<AccessibilityNodeInfo>()
        unvisited.add(nodeInfo)

        while (unvisited.isNotEmpty()) {
            val node = unvisited.removeAt(0) ?: continue
            if ("android.widget.EditText" == node.className.toString() && node.viewIdResourceName == "com.android.chrome:id/url_bar") {
                saveUrl(node.text)
            }
            for (i in 0 until node.childCount) {
                unvisited.add(node.getChild(i))
            }
        }
    }

    private fun saveUrl(text: CharSequence?) {
        if (text == null)
            return
        val pref = getSharedPreferences("webInfo", Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("web_url", text.toString())
        edit.apply()
    }
}