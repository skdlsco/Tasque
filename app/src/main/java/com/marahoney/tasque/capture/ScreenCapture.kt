package com.marahoney.tasque.capture

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScreenCapture(private val context: Context) {

    private val folderPath =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES +
                    File.separator + "Tasque" + File.separator

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val windowSize: Point
        get() {
            val size = Point()
            windowManager.defaultDisplay.getSize(size)
            return size
        }

    private val windowDensity: Int
        get () {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.densityDpi
        }
    private val imageReader = ImageReader.newInstance(windowSize.x, windowSize.y, 0x1, 2)

    private var mediaProjectionManager =
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    var mediaProjection: MediaProjection? = null

    private var virtualDisplay: VirtualDisplay? = null

    private var resultCode: Int? = null
    private var resultData: Intent? = null
    private val dateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.KOREAN)

    fun setMediaProjection(_resultCode: Int? = 0, _resultData: Intent? = null) {
        if (_resultCode != null && _resultData != null) {
            resultCode = _resultCode
            resultData = _resultData
            if (mediaProjection == null)
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode!!, resultData!!)
        }
    }

    fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) {
            return
        }

        this.resultCode = resultCode
        this.resultData = data

        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData!!)
    }

    fun capture(onCaptureSuccess: (String) -> Unit) {
        setMediaProjection()
        if (mediaProjection == null) {
            return
        }

        virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenCapture", windowSize.x, windowSize.y, windowDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.surface, null, null)

        val time = System.currentTimeMillis()
        val fileName = "screenshot_${dateFormat.format(time)}-${time % 1000}.png"
        val image: Image? = imageReader.acquireLatestImage()
        if (image == null) {
            Log.e("ScreenCapture", "image null")
            capture(onCaptureSuccess)
            return
        }
        val width = image.width
        val height = image.height
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        var bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
        image.close()

        virtualDisplay?.release()
        virtualDisplay = null

        if (saveBitmap(bitmap, fileName)) {
            onCaptureSuccess.invoke(File(folderPath, fileName).absolutePath)
        }
    }

    fun saveBitmap(bitmap: Bitmap, fileName: String): Boolean {
        try {
            val fileFolder = File(folderPath)
            if (!fileFolder.exists())
                fileFolder.mkdirs()
            val file = File(folderPath, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            Log.e("ScreenCapture", "success : ${file.absolutePath}")
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    companion object {
        const val REQUEST_MEDIA_PROJECTION = 99
    }
}