package com.marahoney.tasque.ui.splash

class SplashThread : Runnable {

    var callback: (() -> Unit)? = null

    override fun run() {
        Thread.sleep(1000)
        callback?.invoke()
    }
}