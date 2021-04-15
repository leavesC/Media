package github.leavesc.media

import android.app.Activity
import android.content.Intent
import com.zhihu.matisse.MimeType
import github.leavesc.media.activity.ChoosePhotoActivity
import github.leavesc.media.activity.CuttingPhotoActivity
import github.leavesc.media.activity.TakePhotoActivity
import github.leavesc.media.bean.*
import github.leavesc.media.utils.JsonHolder

/**
 * 作者：CZY
 * 时间：2020/5/12 11:01
 * 描述：
 */
object MediaNavigation {

    /*************************************************************************************/

    //跳转到拍照页面
    fun takePhoto(activity: Activity, bean: TakePhotoBean) {
        val intent = Intent(activity, TakePhotoActivity::class.java)
        intent.putExtra(TakePhotoBean::class.java.simpleName, JsonHolder.toJson(bean))
        activity.startActivityForResult(intent, bean.requestCode)
    }

    fun getTakePhotoResult(intent: Intent?): TakePhotoResultBean? {
        val json = intent?.getStringExtra(TakePhotoResultBean::class.java.simpleName)
        if (json.isNullOrBlank()) {
            return null
        }
        return JsonHolder.toBeanOrNull<TakePhotoResultBean>(json)
    }

    fun getTakePhotoPathResult(intent: Intent?): String {
        return getTakePhotoResult(intent)?.imgPath ?: ""
    }

    /*************************************************************************************/

    //跳转到选择图片的页面
    fun choosePhoto(activity: Activity, bean: ChoosePhotoBean) {
        val intent = Intent(activity, ChoosePhotoActivity::class.java)
        if (bean.maxSelectable < 1) {
            return
        }
        //筛选出图片类型，避免外部传入视频类型
        val filter =
            bean.typeList.filter { MimeType.isImage(it.toMatisse().toString()) }.toSet()
        if (filter.isEmpty()) {
            return
        }
        bean.typeList = filter
        //如果可选的图片数量大于 1 或者图片类型包含 gif，则不支持进行裁切
        if (bean.maxSelectable > 1 || filter.find { it == MediaType.GIF } != null) {
            bean.cropEnable = false
        }
        intent.putExtra(ChoosePhotoBean::class.java.simpleName, JsonHolder.toJson(bean))
        activity.startActivityForResult(intent, bean.requestCode)
    }

    fun getChoosePhotoResult(intent: Intent?): ChoosePhotoResultBean? {
        val json = intent?.getStringExtra(ChoosePhotoResultBean::class.java.simpleName)
        if (json.isNullOrBlank()) {
            return null
        }
        return JsonHolder.toBeanOrNull<ChoosePhotoResultBean>(json)
    }

    fun getChoosePhotoPathResult(intent: Intent?): List<String> {
        return getChoosePhotoResult(intent)?.imgPathList ?: emptyList()
    }

    /*************************************************************************************/

    //跳转到裁切图片的页面
    fun cuttingPhoto(activity: Activity, bean: CuttingPhotoBean) {
        val intent = Intent(activity, CuttingPhotoActivity::class.java)
        intent.putExtra(CuttingPhotoBean::class.java.simpleName, JsonHolder.toJson(bean))
        activity.startActivityForResult(intent, bean.requestCode)
    }

    fun getCuttingPhotoResult(intent: Intent?): CuttingPhotoResultBean? {
        val json = intent?.getStringExtra(CuttingPhotoResultBean::class.java.simpleName)
        if (json.isNullOrBlank()) {
            return null
        }
        return JsonHolder.toBeanOrNull<CuttingPhotoResultBean>(json)
    }

    fun getCuttingPhotoPathResult(intent: Intent?): String {
        return getCuttingPhotoResult(intent)?.imagePath ?: ""
    }

    /*************************************************************************************/

//    //跳转到选择媒体资源的页面
//    fun chooseMedia(activity: Activity, bean: ChooseMediaBean) {
//        val intent = Intent(activity, ChooseMediaActivity::class.java)
//        intent.putExtra(ChooseMediaBean::class.java.simpleName, JsonHolder.toJson(bean))
//        activity.startActivityForResult(intent, bean.requestCode)
//    }
//
//    fun getMediaResult(intent: Intent?): ChooseMediaResultBean? {
//        val json = intent?.getStringExtra(ChooseMediaResultBean::class.java.simpleName)
//        if (json.isNullOrBlank()) {
//            return null
//        }
//        return JsonHolder.toBeanOrNull(json, ChooseMediaResultBean::class.java)
//    }
//
//    fun getMediaPathResult(intent: Intent?): List<String> {
//        return getMediaResult(intent)?.pathList ?: mutableListOf()
//    }

}