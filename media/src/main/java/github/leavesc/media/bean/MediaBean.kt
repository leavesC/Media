package github.leavesc.media.bean

import com.zhihu.matisse.MimeType
import java.io.File

/**
 * 作者：CZY
 * 时间：2020/5/12 10:54
 * 描述：
 */

enum class MediaType {

    // ============== images ==============
    JPEG,
    PNG,
    GIF,
    BMP,
    WEBP,

    // ============== videos ==============
    MPEG,
    MP4,
    QUICKTIME,
    THREEGPP,
    THREEGPP2,
    MKV,
    WEBM,
    TS,
    AVI;

    companion object {

        private val imgSuffix = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")

        fun ofImage(hasGif: Boolean = false): Set<MediaType> {
            return mutableSetOf(
                JPEG,
                PNG,
                BMP,
                WEBP
            ).apply {
                if (hasGif) {
                    add(GIF)
                }
            }
        }

        fun ofVideo(): Set<MediaType> {
            return MimeType.ofVideo().map { it.toLocal() }.toSet()
        }

        fun onAll(): Set<MediaType> {
            return MimeType.ofAll().map { it.toLocal() }.toSet()
        }

        fun isImage(filePath: String): Boolean {
            val file = File(filePath)
            if (!file.exists()) {
                return false
            }
            return imgSuffix.find { filePath.endsWith(it, true) } != null
        }

    }

}

internal fun MediaType.toMatisse(): MimeType {
    return when (this) {
        MediaType.JPEG -> {
            MimeType.JPEG
        }
        MediaType.PNG -> {
            MimeType.PNG
        }
        MediaType.GIF -> {
            MimeType.GIF
        }
        MediaType.BMP -> {
            MimeType.BMP
        }
        MediaType.WEBP -> {
            MimeType.WEBP
        }
        MediaType.MPEG -> {
            MimeType.MPEG
        }
        MediaType.MP4 -> {
            MimeType.MP4
        }
        MediaType.QUICKTIME -> {
            MimeType.QUICKTIME
        }
        MediaType.THREEGPP -> {
            MimeType.THREEGPP
        }
        MediaType.THREEGPP2 -> {
            MimeType.THREEGPP2
        }
        MediaType.MKV -> {
            MimeType.MKV
        }
        MediaType.WEBM -> {
            MimeType.WEBM
        }
        MediaType.TS -> {
            MimeType.TS
        }
        MediaType.AVI -> {
            MimeType.AVI
        }
    }
}

internal fun MimeType.toLocal(): MediaType {
    return when (this) {
        MimeType.JPEG -> {
            MediaType.JPEG
        }
        MimeType.PNG -> {
            MediaType.PNG
        }
        MimeType.GIF -> {
            MediaType.GIF
        }
        MimeType.BMP -> {
            MediaType.BMP
        }
        MimeType.WEBP -> {
            MediaType.WEBP
        }
        MimeType.MPEG -> {
            MediaType.MPEG
        }
        MimeType.MP4 -> {
            MediaType.MP4
        }
        MimeType.QUICKTIME -> {
            MediaType.QUICKTIME
        }
        MimeType.THREEGPP -> {
            MediaType.THREEGPP
        }
        MimeType.THREEGPP2 -> {
            MediaType.THREEGPP2
        }
        MimeType.MKV -> {
            MediaType.MKV
        }
        MimeType.WEBM -> {
            MediaType.WEBM
        }
        MimeType.TS -> {
            MediaType.TS
        }
        MimeType.AVI -> {
            MediaType.AVI
        }
    }
}

/*********************拍照*****************************/
/**
 * @param authority FileProvider authority
 * @param cropEnable 是否需要裁切
 * @param compressionQuality 裁切后图片质量，0 到 100 之间
 * @param circleCrop 是否使用圆形裁切框
 */
data class TakePhotoBean(
    val requestCode: Int,
    val authority: String,
    val cropEnable: Boolean = true,
    val compressionQuality: Int = 60,
    val circleCrop: Boolean = false
)

data class TakePhotoResultBean(val imgPath: String)

/*********************选择图片*****************************/

/**
 * @param maxSelectable 选择图片的最大数量
 * @param typeList 支持选择的图片类型，默认支持除了 gif 之外的其它图片类型
 * @param cropEnable 是否需要裁切（当 maxSelectable>1 时无效）
 * @param circleCrop 是否使用圆形裁切框（当 maxSelectable>1 时无效）
 * @param nightTheme 是否使用夜间主题
 */
data class ChoosePhotoBean constructor(
    val requestCode: Int,
    val maxSelectable: Int = 1,
    var typeList: Set<MediaType> = MediaType.ofImage(),
    var cropEnable: Boolean = true,
    val circleCrop: Boolean = false,
    val nightTheme: Boolean = false
)

data class ChoosePhotoResultBean(val imgPathList: List<String>)


/*********************裁切图片*****************************/

/**
 * @param imgUri 图片路径
 * @param compressionQuality 裁切后图片质量，0 到 100 之间
 * @param circleCrop 是否使用圆形裁切框
 */
data class CuttingPhotoBean constructor(
    val requestCode: Int,
    var imgUri: String,
    val compressionQuality: Int = 60,
    val circleCrop: Boolean = false
)

data class CuttingPhotoResultBean(
    val imagePath: String
)

/*********************选择视频+图片资源*****************************/

/**
 * @param maxSelectable 选择Media的最大数量
 * @param typeList 支持选择的 Media 类型，默认支持所有的视频资源，也可以自定义需要的  Media 类型，例如视频+图片
 * @param nightTheme 是否使用夜间主题
 */
data class ChooseMediaBean constructor(
    val requestCode: Int,
    val maxSelectable: Int = 1,
    var typeList: Set<MediaType> = MediaType.ofVideo(),
    val nightTheme: Boolean = false
)

data class ChooseMediaResultBean(val pathList: List<String>)