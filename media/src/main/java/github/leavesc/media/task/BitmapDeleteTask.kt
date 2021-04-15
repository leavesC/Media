package github.leavesc.media.task

import android.app.Activity
import android.os.AsyncTask
import github.leavesc.media.utils.FileUtils
import java.lang.ref.WeakReference

/**
 * 作者：CZY
 * 时间：2020/6/10 18:47
 * 描述：
 */
internal class BitmapDeleteTask(context: Activity) : AsyncTask<Unit, Unit, Unit>() {

    private val context = WeakReference(context)

    override fun doInBackground(vararg params: Unit) {
        try {
            context.get()?.let {
                FileUtils.deleteTimeoutFile(it)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}