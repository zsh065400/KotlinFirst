package cn.zhaoshuhao.kotlinfirst.utils

import cn.zhaoshuhao.kotlinfirst.R
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Scout
 * Created on 2017/8/15 17:07.
 */
fun RequestManager.obtainDefault(): RequestOptions = RequestOptions()
//        .fitCenter()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .priority(Priority.NORMAL)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

