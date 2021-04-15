package github.leavesc.media.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.zhihu.matisse.Matisse
import github.leavesc.media.MediaNavigation
import github.leavesc.media.R
import github.leavesc.media.bean.ChoosePhotoBean
import github.leavesc.media.bean.ChoosePhotoResultBean
import github.leavesc.media.bean.CuttingPhotoBean
import github.leavesc.media.bean.toMatisse
import github.leavesc.media.utils.CoilEngine
import github.leavesc.media.utils.FileUtils
import github.leavesc.media.utils.JsonHolder

/**
 * 作者：CZY
 * 时间：2020/5/12 10:53
 * 描述：
 */
class ChoosePhotoActivity : BaseActivity() {

    companion object {

        private const val REQUEST_CODE_CHOOSE = 0x2000

        private const val REQUEST_CODE_CROP = 0x2001

        private const val REQUEST_CODE_TRANSFORM = 0x2003

    }

    private val choosePhotoBean by lazy {
        JsonHolder.toBean(
            intent.getStringExtra(ChoosePhotoBean::class.java.simpleName),
            ChoosePhotoBean::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navToChose()
    }

    private fun navToChose() {
        Matisse.from(this)
            .choose(choosePhotoBean.typeList.map { it.toMatisse() }.toSet())
            .countable(true)
            .showSingleMediaType(true)
            .maxSelectable(choosePhotoBean.maxSelectable)
            .capture(false)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.70f)
            .imageEngine(CoilEngine())
            .theme(if (choosePhotoBean.nightTheme) R.style.Matisse_Dracula else R.style.Matisse_Zhihu)
            .forResult(REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHOOSE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val obtainPathResult = Matisse.obtainPathResult(data)
                    if (!obtainPathResult.isNullOrEmpty()) {
                        val obtainUriResult = Matisse.obtainResult(data)
                        if (choosePhotoBean.cropEnable && obtainPathResult.size == 1) {
                            navToCuttingPhoto(obtainUriResult[0].toString())
                        } else {
                            setChoosePhotoResultCheck(obtainPathResult, obtainUriResult)
                        }
                        return
                    }
                }
            }
            REQUEST_CODE_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    val cuttingPhotoResult = MediaNavigation.getCuttingPhotoResult(data)
                    cuttingPhotoResult?.let {
                        setChoosePhotoResult(
                            listOf(cuttingPhotoResult.imagePath)
                        )
                        return
                    }
                }
            }
            REQUEST_CODE_TRANSFORM -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result =
                        TransformActivity.getResult(
                            data
                        )
                    if (result.isNotEmpty()) {
                        setChoosePhotoResult(result)
                        return
                    }
                }
                Toast.makeText(this, "发生错误，请重新选择图片", Toast.LENGTH_SHORT).show()
            }
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun navToCuttingPhoto(uriPath: String) {
        MediaNavigation.cuttingPhoto(
            this, CuttingPhotoBean(
                REQUEST_CODE_CROP,
                uriPath,
                60,
                choosePhotoBean.circleCrop
            )
        )
    }

    private fun setChoosePhotoResult(pathList: List<String>) {
        val intent = Intent()
        intent.putExtra(
            ChoosePhotoResultBean::class.java.simpleName,
            JsonHolder.toJson(
                ChoosePhotoResultBean(
                    pathList
                )
            )
        )
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setChoosePhotoResultCheck(pathList: List<String>, uriList: List<Uri>) {
        if (FileUtils.shouldTransformImagePath()) {
            TransformActivity.navTo(
                this,
                REQUEST_CODE_TRANSFORM,
                uriList.map { it.toString() })
        } else {
            setChoosePhotoResult(pathList)
        }
    }

}