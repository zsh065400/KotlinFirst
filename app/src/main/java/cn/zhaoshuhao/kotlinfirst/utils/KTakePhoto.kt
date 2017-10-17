package cn.zhaoshuhao.kotlinfirst.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bilibili.boxing.loader.IBoxingCallback
import com.bilibili.boxing.loader.IBoxingCrop
import com.bilibili.boxing.loader.IBoxingMediaLoader
import com.bilibili.boxing.model.config.BoxingCropOption
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yalantis.ucrop.UCrop

/**
 * Created by Scout
 * Created on 2017/10/16 9:46.
 */
class BoxingGlideLoader : IBoxingMediaLoader {
    override fun displayRaw(img: ImageView, absPath: String, width: Int, height: Int, callback: IBoxingCallback?) {
        val path = "file://" + absPath
        with(Glide.with(img.context)) {
            val options = obtainDefault()
            if (width > 0 && height > 0) {
                options.override(width, height)
            }
            load(path).apply(options).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    if (callback != null) {
                        callback.onFail(e);
                        return true;
                    }
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (resource != null && callback != null) {
                        img.setImageDrawable(resource);
                        callback.onSuccess();
                        return true;
                    }
                    return false;
                }
            }).into(img)
        }
    }

    override fun displayThumbnail(img: ImageView, absPath: String, width: Int, height: Int) {
        val path = "file://" + absPath
        with(Glide.with(img.context)) {
            val options = obtainDefault()
            options.override(width, height)
            load(path).apply(options).into(img)
        }
    }
}

class BoxingUcrop : IBoxingCrop {
    override fun onCropFinish(resultCode: Int, data: Intent?): Uri? {
        if (data == null) {
            return null
        }
        val throwable = UCrop.getError(data)
        return if (throwable != null) {
            null
        } else UCrop.getOutput(data)
    }

    override fun onStartCrop(context: Context?, fragment: Fragment?, cropConfig: BoxingCropOption, path: String, requestCode: Int) {
        val uri = Uri.Builder()
                .scheme("file")
                .appendPath(path)
                .build()
        val crop = UCrop.Options()
        // do not copy exif information to crop pictures
        // because png do not have exif and png is not Distinguishable
        crop.setCompressionFormat(Bitmap.CompressFormat.PNG)
        crop.withMaxResultSize(cropConfig.maxWidth, cropConfig.maxHeight)
        crop.withAspectRatio(cropConfig.aspectRatioX, cropConfig.aspectRatioY)

        UCrop.of(uri, cropConfig.destination)
                .withOptions(crop)
                .start(context!!, fragment!!, requestCode)
    }

}
