package github.leavesc.media.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import coil.load
import com.zhihu.matisse.engine.ImageEngine

/**
 * 作者：CZY
 * 时间：2020/5/12 11:21
 * 描述：
 */
internal class CoilEngine : ImageEngine {

    override fun loadThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(uri) {
            placeholder(placeholder)
            size(resize, resize)
        }
    }

    override fun loadGifThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(uri) {
            placeholder(placeholder)
            size(resize, resize)
        }
    }

    override fun loadImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(uri) {
            size(resizeX, resizeY)
        }
    }

    override fun loadGifImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(uri) {
            size(resizeX, resizeY)
        }
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }

}