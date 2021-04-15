package github.leavesc.media.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import github.leavesc.media.MediaNavigation
import github.leavesc.media.bean.CuttingPhotoBean
import github.leavesc.media.bean.TakePhotoBean
import github.leavesc.media.bean.TakePhotoResultBean
import github.leavesc.media.utils.FileUtils
import github.leavesc.media.utils.JsonHolder
import java.io.File

/**
 * 作者：leavesC
 * 时间：2020/6/3 21:08
 * 描述：
 * GitHub：https://github.com/leavesC
 */
class TakePhotoActivity : BaseActivity() {

    companion object {

        private const val REQUEST_CODE_TAKE_PHOTO = 0x3000

        private const val REQUEST_CODE_CROP = 0x3001

    }

    private val takePhotoBean by lazy {
        JsonHolder.toBean(
            intent.getStringExtra(TakePhotoBean::class.java.simpleName),
            TakePhotoBean::class.java
        )
    }

    private var currentPhotoPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatchTakePictureIntent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imgUri = Uri.fromFile(File(currentPhotoPath)).toString()
                    if (takePhotoBean.cropEnable) {
                        MediaNavigation.cuttingPhoto(
                            this, CuttingPhotoBean(
                                REQUEST_CODE_CROP,
                                imgUri,
                                compressionQuality = takePhotoBean.compressionQuality,
                                circleCrop = takePhotoBean.circleCrop
                            )
                        )
                        return
                    } else {
                        setResult(currentPhotoPath)
                        return
                    }
                }
            }
            REQUEST_CODE_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    val cuttingPhotoResult = MediaNavigation.getCuttingPhotoResult(data)
                    if (cuttingPhotoResult != null) {
                        setResult(cuttingPhotoResult.imagePath)
                        return
                    }
                }
            }
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun setResult(path: String) {
        val intent = Intent()
        val bean = TakePhotoResultBean(path)
        intent.putExtra(TakePhotoResultBean::class.java.simpleName, JsonHolder.toJson(bean))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resolveActivity = intent.resolveActivity(packageManager)
        if (resolveActivity != null) {
            try {
                val photoFile = File.createTempFile(
                    FileUtils.createImageName,
                    FileUtils.SUFFIX,
                    externalCacheDir
                ).apply {
                    currentPhotoPath = absolutePath
                }
                val photoURI = FileProvider.getUriForFile(
                    this,
                    takePhotoBean.authority,
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
                return
            } catch (ex: Throwable) {

            }
        } else {
            Toast.makeText(this, "请检查相机是否正常", Toast.LENGTH_SHORT).show()
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}