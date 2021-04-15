package github.leavesc.media.activity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import github.leavesc.media.task.BitmapDeleteTask

/**
 * 作者：CZY
 * 时间：2020/6/10 18:56
 * 描述：
 */
open class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        BitmapDeleteTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

}