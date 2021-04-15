package github.leavesc.media.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.zhihu.matisse.Matisse
import github.leavesc.media.R
import github.leavesc.media.bean.ChooseMediaBean
import github.leavesc.media.bean.ChooseMediaResultBean
import github.leavesc.media.bean.toMatisse
import github.leavesc.media.utils.FileUtils
import github.leavesc.media.utils.CoilEngine
import github.leavesc.media.utils.JsonHolder

/**
 * 作者：CZY
 * 时间：2020/5/12 22:11
 * 描述：
 */
@Deprecated("")
class ChooseMediaActivity : BaseActivity() {

    private companion object {

        private const val REQUEST_CODE_MEDIA = 0x1000

    }

    private val chooseMediaBean by lazy {
        JsonHolder.toBean(
            intent.getStringExtra(ChooseMediaBean::class.java.simpleName),
            ChooseMediaBean::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navToChose()
    }

    private fun navToChose() {
        Matisse.from(this)
            .choose(chooseMediaBean.typeList.map { it.toMatisse() }.toSet())
            .countable(true)
            .showSingleMediaType(true)
            .maxSelectable(chooseMediaBean.maxSelectable)
            .capture(false)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.70f)
            .imageEngine(CoilEngine())
            .theme(if (chooseMediaBean.nightTheme) R.style.Matisse_Dracula else R.style.Matisse_Zhihu)
            .forResult(REQUEST_CODE_MEDIA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_MEDIA -> {
                if (resultCode == Activity.RESULT_OK) {
                    val obtainPathResult = Matisse.obtainPathResult(data)
                    val obtainUriResult = Matisse.obtainResult(data)
                    if (!obtainPathResult.isNullOrEmpty() && !obtainUriResult.isNullOrEmpty() && obtainPathResult.size == obtainUriResult.size) {
                        if (FileUtils.shouldTransformImagePath()) {
                            navToTransformImage(
                                obtainUriResult.map { it.toString() }
                            )
                        } else {
                            setResult(obtainPathResult)
                        }
                        return
                    }
                } else {
                    setResult(resultCode)
                }
            }
        }
        finish()
    }

    private fun setResult(pathList: List<String>) {
        val intent = Intent()
        intent.putExtra(
            ChooseMediaResultBean::class.java.simpleName,
            JsonHolder.toJson(
                ChooseMediaResultBean(
                    pathList
                )
            )
        )
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun navToTransformImage(uriList: List<String>) {
//        val intent = Intent()
//        intent.putExtra(
//            ChooseMediaResultBean::class.java.simpleName,
//            JsonHolder.toJson(ChooseMediaResultBean(pathList))
//        )
//        setResult(Activity.RESULT_OK, intent)
//        finish()
    }

}