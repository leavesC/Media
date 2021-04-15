package github.leavesc.media.activity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import github.leavesc.media.R
import github.leavesc.media.task.BitmapTransformCallback
import github.leavesc.media.task.BitmapTransformTask
import github.leavesc.media.utils.JsonHolder

/**
 * 作者：CZY
 * 时间：2020/6/9 15:32
 * 描述：
 */
class TransformActivity : BaseActivity() {

    internal companion object {

        private const val KEY_URI = "keyUriList"

        private const val KEY_RESULT_PATH_LIST = "keyResultPathList"

        fun navTo(context: Activity, requestCode: Int, uri: List<String>) {
            val intent = Intent(context, TransformActivity::class.java)
            intent.putExtra(KEY_URI, JsonHolder.toJson(uri))
            context.startActivityForResult(intent, requestCode)
        }

        fun getResult(data: Intent?): List<String> {
            if (data == null) {
                return emptyList()
            }
            return JsonHolder.toBeanOrDefault(
                data.getStringExtra(KEY_RESULT_PATH_LIST),
                emptyList()
            )
        }

    }

    private val uriList by lazy {
        JsonHolder.toBean<List<String>>(
            intent.getStringExtra(
                KEY_URI
            )
        )
    }

    private val bitmapTransformCallback = object : BitmapTransformCallback {

        override fun onTransformed(pathList: List<String>) {
            val intent = Intent()
            intent.putExtra(KEY_RESULT_PATH_LIST, JsonHolder.toJson(pathList))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        override fun onFailure() {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform)
        BitmapTransformTask(
            this,
            uriList,
            bitmapTransformCallback
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

}