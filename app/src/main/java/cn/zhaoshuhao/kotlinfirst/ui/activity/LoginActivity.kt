package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobSmsState
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.utils.doEncrypt
import cn.zhaoshuhao.kotlinfirst.utils.findViewOften
import cn.zhaoshuhao.kotlinfirst.utils.obtain
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert

class LoginActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_login

    override fun prepareInitUI() {
//        BmobSMS.initialize(this, BMOB_ID);
    }

    override fun beforeInitViews() {

    }

    private var phone: String = ""
    private var code: String = ""

    override fun initViews() {
        id_login_tv_register.setOnClickListener {
            register()
        }
        id_login_btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val phone = id_login_et_phone.obtain().doEncrypt()
        val pwd = id_login_et_pwd.obtain().doEncrypt()
        logd("$phone, $pwd")
        if (!phone.isNullOrBlank() && !pwd.isNullOrBlank()) {
            val user = User(null, "", "", "", "")
            user.setUsername(phone)
            user.setPassword(pwd)
            user.login(object : SaveListener<User>() {
                override fun done(p0: User?, e: BmobException?) {
                    if (e == null) {
                        //登录成功
                        toast("登录成功")
                        val currentUser = BmobUser.getCurrentUser(User::class.java)
                        val intent = Intent()
                        val bundle = Bundle()
                        bundle.putSerializable("userinfo", currentUser)
                        intent.putExtras(bundle)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        toast("账号或密码错误")
                        logd("${e.errorCode}  ${e.localizedMessage}")
                    }
                }
            })
        }
    }

    private var phoneDialog: AlertDialog? = null

    private fun register() {
        val alert = alert { }
        val view = obtainRegisterView(0)
        alert.customView = view
        phoneDialog = alert.build() as AlertDialog
        //            dialog.setCancelable(false)
        phoneDialog!!.setCanceledOnTouchOutside(false)
        phoneDialog!!.setButton(DialogInterface.BUTTON_POSITIVE, "发送验证码") { _, _ ->
            val editText = view.findViewOften<MaterialEditText>(R.id.id_register_et_content)
            val b = editText.isValid("^1(3|4|5|7|8)\\d{9}\$")
            if (b) {
                phone = editText.text.toString().trim()
                sendSMSCode(phone)
            } else {
                toast("手机号码有误，请重新输入")
            }
        }
        phoneDialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, "取消") { dialogInterface, i ->
            phoneDialog!!.showing(true)
            phoneDialog!!.dismiss()
        }
        phoneDialog!!.show()
        phoneDialog!!.showing(false)
    }

    private var verifyDialog: AlertDialog? = null

    private fun verifyCode() {
        val alert = alert { }
        val view = obtainRegisterView(1)
        alert.customView = view
        verifyDialog = alert.build() as AlertDialog
        //            dialog.setCancelable(false)
        verifyDialog!!.setCanceledOnTouchOutside(false)
        verifyDialog!!.setButton(DialogInterface.BUTTON_POSITIVE, "验证") { _, _ ->
            val editText = view.findViewOften<MaterialEditText>(R.id.id_register_et_content)
            val b = editText.isValid("\\d{6}")
            if (b) {
                code = editText.obtain()
                verifySMSCode(phone, code)
            } else {
                toast("验证码错误")
            }
        }
        verifyDialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, "取消") { _, _ ->
            verifyDialog!!.showing(true)
            verifyDialog!!.dismiss()
        }
        verifyDialog!!.show()
        verifyDialog!!.showing(false)
    }

    private fun sendSMSCode(phone: String) {
        BmobSMS.requestSMSCode(phone, "毕业设计", object : QueryListener<Int>() {
            override fun done(smsId: Int?, ex: BmobException?) {
                if (ex == null) {
                    querySMSState(smsId!!)
                    toast("验证码已发送")
                    verifyCode()
                    phoneDialog!!.showing(true)
                    phoneDialog!!.dismiss()
                } else {
                    toast("验证码发送失败")
                    logd("${ex?.errorCode} ${ex?.localizedMessage}")
                }
            }
        })
    }

    private fun querySMSState(smsId: Int) {
        BmobSMS.querySmsState(smsId, object : QueryListener<BmobSmsState>() {
            override fun done(state: BmobSmsState?, e: BmobException?) {
                if (e == null)
                    when (state?.smsState) {
                        "SUCCESS" -> toast("验证码发送成功")
                        "FAIL" -> toast("验证码发送失败")
                        "SENDING" -> toast("验证码发送中...")
                    }
            }
        })
    }

    private fun verifySMSCode(phone: String, code: String) {
        BmobSMS.verifySmsCode(phone, code, object : UpdateListener() {
            override fun done(ex: BmobException?) {
                if (ex == null) {
                    toast("验证成功")
                    verifyDialog?.showing(true)
                    verifyDialog?.dismiss()
                    inputPwd()
                } else toast("验证失败 错误码${ex.errorCode}")
            }
        })
    }

    private var password = ""
    private var inputDialog: AlertDialog? = null

    private fun inputPwd() {
        val alert = alert { }
        val view = obtainRegisterView(2)
        alert.customView = view
        inputDialog = alert.build() as AlertDialog
        inputDialog!!.setCanceledOnTouchOutside(false)
        inputDialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, "取消") { _, _ ->
            inputDialog!!.showing(true)
            inputDialog!!.dismiss()
        }
        inputDialog!!.setButton(DialogInterface.BUTTON_POSITIVE, "确定") { _, _ ->
            val editText = view.findViewOften<MaterialEditText>(R.id.id_register_et_content)
            val b = editText.isValid("[a-zA-Z0-9]{6,12}")
            if (b) {
                password = editText.obtain()
                registerDone()
            } else {
                toast("密码为空或不符合规则")
            }
        }
        inputDialog!!.show()
        inputDialog!!.showing(false)
    }

    /*
    *
    * */
    private fun registerDone() {
        val user = User(null, "", "", "", "")
        user.setUsername(phone.doEncrypt())
        user.setPassword(password.doEncrypt())
        user.mobilePhoneNumber = phone
        user.mobilePhoneNumberVerified = true
        user.signUp(object : SaveListener<User>() {
            override fun done(user: User?, e: cn.bmob.v3.exception.BmobException?) {
                if (e == null) {
                    toast("注册成功，请登录")
                    inputDialog!!.showing(true)
                    inputDialog!!.dismiss()
                    id_login_et_phone.setText(phone)
                    id_login_et_pwd.setText(password)
                    /*清除注册的信息，保证注册后不会缓存用户信息*/
                    BmobUser.logOut()
                } else {
                    logd("${e.errorCode} ${e.localizedMessage}")
                }
            }
        })
    }

    private fun AlertDialog.showing(b: Boolean) {
        val field = this::class.java.superclass.getDeclaredField("mShowing")
        field.isAccessible = true
        field.set(this, b)
    }

    private fun obtainRegisterView(i: Int): View {
        val inflate = layoutInflater.inflate(R.layout.alert_register_basic, null, false)
        val editText = inflate.findViewOften<MaterialEditText>(R.id.id_register_et_content)
        when (i) {
            0 -> {//发送验证码
                editText.hint = "请输入手机号"
                editText.inputType = InputType.TYPE_CLASS_PHONE
                editText.maxCharacters = 11
                editText.minCharacters = 11
            }
            1 -> {//验证
                editText.hint = "请输入验证码"
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.maxCharacters = 6
                editText.minCharacters = 6
            }
            2 -> {//注册
                editText.hint = "请输入密码"
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                editText.maxCharacters = 12
                editText.minCharacters = 6
            }
        }
        return inflate
    }
}
