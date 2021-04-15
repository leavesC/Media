package github.leavesc.mediasamples

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import coil.load
import github.leavesc.media.MediaNavigation
import github.leavesc.media.bean.ChoosePhotoBean
import github.leavesc.media.bean.TakePhotoBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), 100
        )

        btn_choosePhoto.setOnClickListener {
            MediaNavigation.choosePhoto(
                this,
                ChoosePhotoBean(
                    100,
                    1,
                    nightTheme = true,
                    cropEnable = true,
                    circleCrop = false
                )
            )
        }
        btn_takePhoto.setOnClickListener {
            MediaNavigation.takePhoto(
                this,
                TakePhotoBean(
                    400,
                    "leavesc.media.samples.FileProvider"
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val choosePhotoResult = MediaNavigation.getChoosePhotoResult(data)
                    tv_log.text = choosePhotoResult?.toString() ?: "error"
                    choosePhotoResult?.imgPathList?.getOrNull(0)?.let {
                        iv_image.load(it)
                    }
                }
            }
            400 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val getTakePhotoResult = MediaNavigation.getTakePhotoResult(data)
                    tv_log.text = getTakePhotoResult?.toString() ?: "error"
                    getTakePhotoResult?.let {
                        iv_image.load(it.imgPath)
                    }
                }
            }
        }
    }

}
