package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.utils.*
import com.bilibili.boxing.Boxing
import com.bilibili.boxing.BoxingCrop
import com.bilibili.boxing.BoxingMediaLoader
import com.bilibili.boxing.model.config.BoxingConfig
import com.bilibili.boxing.model.config.BoxingCropOption
import com.bilibili.boxing_impl.ui.BoxingActivity
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_personal_info.*
import org.jetbrains.anko.alert
import java.io.File


class PersonalInfoActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_personal_info

    //TODO 可以通过Application等对User对象进行内存缓存，节省调用时间
    val user: User
        get() = BmobUser.getCurrentUser(User::class.java)

    override fun prepareInitUI() {
        BoxingMediaLoader.getInstance().init(BoxingGlideLoader())
        BoxingCrop.getInstance().init(BoxingUcrop())
    }

    override fun initViews() {
        id_personal_iv_head.loadUrl(this, user.head?.fileUrl ?: "")
        id_personal_tv_nick.text = user.nick ?: "未填写"
        id_personal_tv_age.text = user.age ?: "未填写"
        id_personal_tv_sex.text = user.sex ?: "未填写"
        id_personal_tv_email.text = user.email ?: "未填写"
        id_personal_tv_address.text = user.address ?: "未填写"
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_personal_toolbar as Toolbar,
                "个人信息") { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = Boxing.getResult(data);
        if (result != null && result.isNotEmpty()
                && requestCode == 0x10) {
            val path = result[0].path
            logd(path)
            val file = BmobFile(File(path))
            file.uploadblock(object : UploadFileListener() {
                override fun done(e: BmobException?) {
                    if (e == null) {
                        val newUser = User(file, user.nick, user.sex, user.address, user.age)
                        newUser.update(user.objectId, object : UpdateListener() {
                            override fun done(e: BmobException?) {
                                if (e == null) {
                                    toast("修改成功")
                                    id_personal_iv_head.loadUrl(this@PersonalInfoActivity, file?.fileUrl)
                                } else {
                                    toast("修改失败")
                                    logd(e.localizedMessage)
                                }
                            }
                        })
                    } else {
                        toast("图片上传失败")
                        logd(e.localizedMessage)
                    }
                }

                override fun onProgress(value: Int?) {
                    super.onProgress(value)
                }
            })
        }
    }

    fun changeInfo(v: View) {
        v as LinearLayout
        val child = v.getChildAt(2)
        when (child.id) {
            R.id.id_personal_iv_head -> {
                /*图片选择加载*/
                takePhoto()
            }
            R.id.id_personal_tv_nick -> {
                obtainDialog(0, child)
            }
            R.id.id_personal_tv_age -> {
                obtainDialog(1, child)
            }
            R.id.id_personal_tv_sex -> {
                obtainDialog(2, child)
            }
            R.id.id_personal_tv_email -> {
                obtainDialog(3, child)
            }
            R.id.id_personal_tv_address -> {
                obtainDialog(4, child)
            }
        }
    }

    private fun takePhoto() {
        val cache = "${(externalCacheDir ?: cacheDir).absolutePath}/tmp.jpg"
        val uri = Uri.parse(cache)
        val config = BoxingConfig(BoxingConfig.Mode.SINGLE_IMG).withCropOption(BoxingCropOption(uri).withMaxResultSize(200, 200))
        config.needCamera(R.mipmap.ic_launcher).withMaxCount(1)
        Boxing.of(config).withIntent(this@PersonalInfoActivity, BoxingActivity::class.java).start(this@PersonalInfoActivity, 0x10);
    }

    private fun obtainDialog(i: Int, view: View) = with(alert { }) {
        val obtainView = obtainView(i)
        customView = obtainView
        val editText = obtainView.findViewOften<MaterialEditText>(R.id.id_register_et_content)
        negativeButton("取消") {}
        positiveButton("确定") {
            val text = editText.obtain()
            val newUser = User(user.head, user.nick, user.sex, user.address, user.age)
            when (i) {
            /*个人信息需要对有效性进行检测，此处不做额外处理，仅实现逻辑*/
                0 -> {//昵称
                    newUser.nick = text
                }
                1 -> {//年龄
                    newUser.age = text
                }
                2 -> {//性别
                    newUser.sex = text
                }
                3 -> {//邮箱
                    newUser.setEmail(text)
                }
                4 -> {//所在地
                    newUser.address = text
                }
            }
            newUser.update(user.objectId, object : UpdateListener() {
                override fun done(e: BmobException?) {
                    if (e == null) {
                        (view as TextView).text = text
                        toast("修改成功")
                    } else toast("修改失败")
                }
            })
        }
        show()
    }

    val view: View
        get() = layoutInflater.inflate(R.layout.alert_register_basic, null, false)

    private fun obtainView(i: Int): View {
        val editText = view.findViewOften<MaterialEditText>(R.id.id_register_et_content)
        when (i) {
        /*个人信息需要对有效性进行检测，此处不做额外处理，仅实现逻辑*/
            0 -> {//昵称
                editText.hint = "昵称"
                editText.maxCharacters = 10
                editText.minCharacters = 3
            }
            1 -> {//年龄
                editText.hint = "年龄"
                editText.maxCharacters = 3
                editText.minCharacters = 1
            }
            2 -> {//性别
                editText.hint = "性别"
                editText.maxCharacters = 1
                editText.minCharacters = 1
            }
            3 -> {//邮箱
                editText.hint = "邮箱"
                editText.maxCharacters = 20
                editText.minCharacters = 5
            }
            4 -> {//所在地
                editText.hint = "所在地"
                editText.maxCharacters = 15
                editText.minCharacters = 1
            }
        }
        return view
    }
}
