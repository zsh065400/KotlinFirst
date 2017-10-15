package cn.zhaoshuhao.kotlinfirst.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.bmob.v3.BmobUser
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseFragment
import cn.zhaoshuhao.kotlinfirst.base.toActivityForResult
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.ui.activity.HistoryActivity
import cn.zhaoshuhao.kotlinfirst.ui.activity.LoginActivity
import cn.zhaoshuhao.kotlinfirst.ui.activity.OrderDetailActivity
import cn.zhaoshuhao.kotlinfirst.ui.activity.StarActivity
import kotlinx.android.synthetic.main.fragment_mine.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class MineFragment : BaseFragment() {
    override fun obtainLayoutID(): Int = R.layout.fragment_mine

    private var user: User? = null

    override fun initView(view: View?, savedBundle: Bundle?) {
        user = BmobUser.getCurrentUser(User::class.java)
        if (user != null) {
            initUserInfo(user!!)
        }

        id_mine_civ_head.setOnClickListener(toLogin)
        id_mine_tv_nick.setOnClickListener(toLogin)
        id_mine_btn_history.setOnClickListener {
            startActivity<HistoryActivity>()
        }
        id_mine_btn_star.setOnClickListener {
            if (BmobUser.getCurrentUser(User::class.java) == null)
                toast("请先登录")
            else
                startActivity<StarActivity>()
        }
        id_mine_btn_order.setOnClickListener(toOrder)
        id_mine_tv_nopay.setOnClickListener(toOrder)
        id_mine_tv_paydone.setOnClickListener(toOrder)
    }

    private val toOrder: (View) -> Unit = {
        var index = 0
        when(it.id){
            R.id.id_mine_tv_nopay -> index = 2
            R.id.id_mine_tv_paydone -> index = 1
        }
        if (BmobUser.getCurrentUser(User::class.java) == null)
            toast("请先登录")
        else {
            val intent = Intent(activity, OrderDetailActivity::class.java)
            intent.putExtra("index", index)
            startActivity(intent)
        }
    }

    private val toLogin: (View) -> Unit = {
        if (user == null) toActivityForResult<LoginActivity>(0x12)
        else {
            with(alert("是否退出登录", "登出")) {
                positiveButton("是") {
                    id_mine_tv_nick.text = "您还没有登录哦"
                    BmobUser.logOut()
                    user = null
                }
                negativeButton("否") { }
                show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x12 && resultCode == RESULT_OK) {
            val user: User = data?.extras?.getSerializable("userinfo") as User
            initUserInfo(user)
        }
    }

    private fun initUserInfo(user: User) {
        this.user = user
        val nick = user.nick
        id_mine_tv_nick.text = if (nick.isNullOrEmpty()) user.mobilePhoneNumber else "请设置昵称"
    }


}
