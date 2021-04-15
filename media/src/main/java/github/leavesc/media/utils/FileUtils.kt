package github.leavesc.media.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * 作者：CZY
 * 时间：2020/6/8 15:49
 * 描述：
 */
internal object FileUtils {

    const val SUFFIX = ".jpg"

    @SuppressLint("ConstantLocale")
    private val DATA_FORMAT = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    private val today: String
        get() = DATA_FORMAT.format(Date())

    val createImageName: String
        get() = SimpleDateFormat(
            "yyyyMMdd_HH_mm_ss_ssss",
            Locale.getDefault()
        ).format(Date()) + "_${
        (Random.nextInt(
            1,
            3000
        ))
        }"

    private fun getCacheDirPath(context: Context): String {
        return (context.externalCacheDir
            ?: context.cacheDir).absolutePath + File.separatorChar + "photoCache"
    }

    private fun createImagePath(context: Context): String {
        return getCacheDirPath(context) + File.separatorChar + today + File.separatorChar + createImageName + SUFFIX
    }

    fun createImageFile(context: Context): File {
        val file = File(
            createImagePath(
                context
            )
        )
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        return file
    }

    private fun bitmapToFile(context: Context, bitmap: Bitmap): File? {
        var fileOutputStream: FileOutputStream? = null
        try {
            val file =
                createImageFile(context)
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream)
            fileOutputStream.flush()
            return file
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            fileOutputStream?.let {
                try {
                    it.close()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            try {
                bitmap.recycle()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun copyToInnerCache(context: Context, imgUri: String): File? {
        var fd: ParcelFileDescriptor? = null
        var bitmap: Bitmap? = null
        try {
            fd = context.applicationContext.contentResolver.openFileDescriptor(
                Uri.parse(imgUri),
                "r"
            )
            fd?.let {
                val temp = BitmapFactory.decodeFileDescriptor(fd.fileDescriptor)
                bitmap = temp
                return bitmapToFile(
                    context,
                    temp
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            try {
                fd?.close()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            try {
                bitmap?.recycle()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun shouldTransformImagePath(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun deleteTimeoutFile(context: Activity) {
        val cacheDir = File(
            getCacheDirPath(
                context
            )
        )
        if (!cacheDir.exists()) {
            return
        }
        val listFiles = cacheDir.listFiles()
        if (listFiles.isNullOrEmpty()) {
            return
        }

        val calendar = Calendar.getInstance()
        val today = DATA_FORMAT.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = DATA_FORMAT.format(calendar.time)

        val filter = listFiles.filter {
            !it.name.contains(today) && !it.name.contains(yesterday)
        }

        filter.forEach {
            it.deleteRecursively()
        }
    }

}