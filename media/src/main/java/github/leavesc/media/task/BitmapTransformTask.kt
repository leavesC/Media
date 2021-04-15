package github.leavesc.media.task

import android.app.Activity
import android.net.Uri
import android.os.AsyncTask
import com.yalantis.ucrop.util.BitmapLoadUtils
import github.leavesc.media.utils.FileUtils
import java.io.File
import java.lang.ref.WeakReference

/**
 * 作者：CZY
 * 时间：2020/6/10 11:01
 * 描述：
 */
internal interface BitmapTransformCallback {

    fun onTransformed(pathList: List<String>)

    fun onFailure()

}

internal class BitmapTransformTask(
    context: Activity,
    private val uriList: List<String>,
    private var bitmapTransformCallback: BitmapTransformCallback?
) : AsyncTask<Unit, Unit, List<String>?>() {

    private val context = WeakReference(context)

    override fun doInBackground(vararg params: Unit): List<String>? {
        return if (uriList.isEmpty()) {
            null
        } else {
            val resultList = mutableListOf<String>()
            uriList.forEach {
                try {
                    val get = context.get() ?: return null
                    val maxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(get)
                    val outputFileUri = Uri.fromFile(FileUtils.createImageFile(get))
                    val task = BitmapLoadTask(
                        get,
                        Uri.parse(it),
                        outputFileUri,
                        maxBitmapSize,
                        maxBitmapSize
                    )
                    val result = task.doInBackground()
                    if (result.mBitmapWorkerException == null) {
                        val imagePath = outputFileUri.path
                        if (imagePath.isNullOrBlank() || !File(imagePath).exists()) {

                        } else {
                            resultList.add(imagePath)
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            resultList
        }
    }

    override fun onPostExecute(result: List<String>?) {
        context.get() ?: return
        val callback = bitmapTransformCallback
        callback?.let {
            if (result == null) {
                callback.onFailure()
            } else {
                callback.onTransformed(result)
            }
            bitmapTransformCallback = null
        }
    }

}