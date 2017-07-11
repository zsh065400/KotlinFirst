package cn.zhaoshuhao.kotlinfirst

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        id_tv_hello.text = "Hello Kotlin, I't coming ${id_tv_hello.text}"
        id_tv_hello.setOnClickListener { showToast }
    }

    val showToast = { _: View -> Toast.makeText(this, "${id_tv_hello.text}", Toast.LENGTH_LONG).show() }
}


