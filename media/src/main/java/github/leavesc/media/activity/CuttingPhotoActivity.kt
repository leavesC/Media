package github.leavesc.media.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import com.yalantis.ucrop.UCrop
import github.leavesc.media.bean.CuttingPhotoBean
import github.leavesc.media.bean.CuttingPhotoResultBean
import github.leavesc.media.utils.FileUtils
import github.leavesc.media.utils.JsonHolder
import java.io.File

/**
 * 作者：CZY
 * 时间：2020/5/12 10:54
 * 描述：
 */
class CuttingPhotoActivity : BaseActivity() {

    private val cuttingPhotoBean by lazy {
        JsonHolder.toBean(
            intent.getStringExtra(CuttingPhotoBean::class.java.simpleName),
            CuttingPhotoBean::class.java
        )
    }

    private var outputFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCrop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        val path = outputFile?.path
                        if (!path.isNullOrBlank()) {
                            val uri = UCrop.getOutput(data)
                            if (uri != null) {
                                setResult(path, uri)
                                return
                            }
                        }
                    }
                }
            }
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun setResult(path: String, uri: Uri) {
        val intent = Intent()
        intent.putExtra(
            CuttingPhotoResultBean::class.java.simpleName,
            JsonHolder.toJson(
                CuttingPhotoResultBean(
                    path
                )
            )
        )
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun startCrop() {
        //裁剪后图片保存的文件
        outputFile = FileUtils.createImageFile(this)
        val uCrop = UCrop.of(
            Uri.parse(cuttingPhotoBean.imgUri),
            Uri.fromFile(outputFile)
        )//第一个参数是裁剪前的uri,第二个参数是裁剪后的uri
        uCrop.withAspectRatio(1f, 1f)//设置裁剪框的宽高比例
        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(cuttingPhotoBean.compressionQuality)
        options.setCircleDimmedLayer(cuttingPhotoBean.circleCrop)//设置是否为圆形裁剪框
//        //下面参数分别是缩放,旋转,裁剪框的比例
//        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)
//        options.setToolbarTitle(null)//设置标题栏文字
//        options.setCropGridStrokeWidth(2)//设置裁剪网格线的宽度
//        options.setCropFrameStrokeWidth(10)//设置裁剪框的宽度
//        options.setMaxScaleMultiplier(4f)//设置最大缩放比例
//        options.setHideBottomControls(true)//隐藏下边控制栏
//        options.setShowCropGrid(true)  //设置是否显示裁剪网格
//        options.setShowCropFrame(true) //设置是否显示裁剪边框(true为方形边框)
//        options.setFreeStyleCropEnabled(true)
//        options.setToolbarWidgetColor(Color.parseColor("#ffffff"))//标题字的颜色以及按钮颜色
//        options.setDimmedLayerColor(Color.parseColor("#AA000000"))//设置裁剪外颜色
//        options.setToolbarColor(Color.parseColor("#000000")) // 设置标题栏颜色
//        options.setStatusBarColor(Color.parseColor("#000000"))//设置状态栏颜色
//        options.setCropGridColor(Color.parseColor("#ffffff"))//设置裁剪网格的颜色
//        options.setCropFrameColor(Color.parseColor("#ffffff"))//设置裁剪框的颜色
        uCrop.withOptions(options)
        uCrop.start(this)
    }

}