package cn.zhaoshuhao.kotlinfirst

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        id_tv_hello.text = "Hello Kotlin, I't coming ${id_tv_hello.text}"
        id_tv_hello.setOnClickListener { }
    }
}

fun <T> Activity.startActivity(other: Class<T>) {
    val target = Intent(this, other)
    startActivity(target)
    //设置进入和退出动画
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    finish()
}

inline fun <reified T : Context> Activity.startActivity() {
    startActivity(T::class.java)
}

