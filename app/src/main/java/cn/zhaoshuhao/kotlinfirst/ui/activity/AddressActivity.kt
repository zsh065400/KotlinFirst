package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.AddressAdapter
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toActivityForResult
import cn.zhaoshuhao.kotlinfirst.model.bean.Address
import cn.zhaoshuhao.kotlinfirst.model.db.KAddressDao
import cn.zhaoshuhao.kotlinfirst.utils.anyToJson
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import cn.zhaoshuhao.kotlinfirst.utils.toList
import cn.zhaoshuhao.kotlinfirst.utils.vertical
import kotlinx.android.synthetic.main.activity_address.*
import org.jetbrains.anko.alert

class AddressActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_address
    private val addrDao = KAddressDao.get(this)
    private var datas: ArrayList<Address> = arrayListOf()
    private var adapter: AddressAdapter? = null

    override fun initToolbar() {
        defaultToolbarOptions(id_address_toolbar as Toolbar, "收货地址") { finish() }
    }

    override fun beforeInitViews() {
        refreshDatas()
    }

    private fun refreshDatas() {
        datas = addrDao.queryAll()?.toList<Address>()
        datas.sortBy { it.default == "false" }
        if (adapter != null) {
            adapter?.reset(datas)
        }
    }

    override fun initViews() {
        initRecycleView()
        initAddButton()
    }

    private fun addOrUpdate(bundle: Bundle = Bundle()) {
        toActivityForResult<AddressEditActivity>(bundle, 0x124)
    }

    private fun initAddButton() {
        id_address_fab_add.setOnClickListener {
            addOrUpdate()
        }
    }

    private fun initRecycleView() {
        adapter = AddressAdapter(this@AddressActivity, datas, null)
        with(adapter!!) {
            setOnItmeClickListener(object : BaseSupportAdapter.OnItemClickListener<Address> {
                override fun onClick(view: View, data: Address, position: Int) {
                    val intent = Intent()
                    val bundle = Bundle()
                    bundle.putParcelable("address", data)
                    intent.putExtras(bundle)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
            setOnItmeLongClickListener(object : BaseSupportAdapter.OnItemLongClickListener<Address> {
                override fun onLongClick(view: View, data: Address, position: Int) {
                    val alert = alert {}
                    alert.title = "请选择"
                    alert.items(listOf("编辑", "删除", "设为默认")) { dialog, index ->
                        when (index) {
                            0 -> {
                                val bundle = Bundle()
                                bundle.putParcelable("address", data)
                                addOrUpdate(bundle)
                            }
                            1 -> {
                                addrDao.deleteForAbs("UUID", data.UUID)
                                refreshDatas()
                            }
                            2 -> {
                                changeDefaultAddress(data)
                            }
                        }
                    }
                    alert.show()
                }
            })
        }
        with(id_address_rv_list) {
            vertical(this@AddressActivity)
            this.adapter = this@AddressActivity.adapter
        }
    }

    private fun changeDefaultAddress(data: Address) {
        datas.forEach {
            it.default = "false"
            if (it.UUID == data.UUID)
                it.default = "true"
            addrDao.updateForAbs("UUID", it.UUID, Pair("json", anyToJson(it)))
        }
        refreshDatas()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x124) {
            if (resultCode == 5) {/*新加内容*/
                refreshDatas()
            } else if (resultCode == 6) {/*更新内容*/
                refreshDatas()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        addrDao.onDestroy()
    }
}
