package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.graphics.Color
import android.support.v7.widget.Toolbar
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.Address
import cn.zhaoshuhao.kotlinfirst.model.db.KAddressDao
import cn.zhaoshuhao.kotlinfirst.utils.anyToJson
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import com.lljjcoder.city_20170724.CityPickerView
import com.lljjcoder.city_20170724.bean.CityBean
import com.lljjcoder.city_20170724.bean.DistrictBean
import com.lljjcoder.city_20170724.bean.ProvinceBean
import kotlinx.android.synthetic.main.activity_address_edit.*


class AddressEditActivity : BaseActivity() {
    private val addrDao = KAddressDao.get(this)
    private var address: Address? = null
    private var UUID: String? = null

    override fun obtainLayoutID(): Int = R.layout.activity_address_edit

    override fun initToolbar() {
        defaultToolbarOptions(id_edit_toolbar as Toolbar, "编辑收货信息") { finish() }
    }

    override fun beforeInitViews() {
        address = intent?.extras?.getParcelable("address")
    }

    override fun initViews() {
        if (address != null) {
            UUID = address!!.UUID
            id_edit_et_person.setText(address!!.name)
            id_edit_et_phone.setText(address!!.phone)
            id_edit_et_part.setText(address!!.part)
            id_edit_et_detail.setText(address!!.detail)
        }

        id_edit_fab_done.setOnClickListener {
            val person = id_edit_et_person.text.toString() ?: ""
            val phone = id_edit_et_phone.text.toString() ?: ""
            val part = id_edit_et_part.text.toString() ?: ""
            val detail = id_edit_et_detail.text.toString() ?: ""
            if (person.isNotBlank() && phone.isNotBlank() && part.isNotBlank() && detail.isNotBlank()) {
                if (UUID != null) {
                    val addr = Address(UUID!!, person, phone, part, detail)
                    addrDao.updateForAbs("UUID", UUID!!, Pair("json", anyToJson(addr)))
                    setResult(6)
                } else {
                    val addr = Address(java.util.UUID.randomUUID().toString(), person, phone, part, detail)
                    addrDao.insert(Pair("UUID", addr.UUID), Pair("json", anyToJson(addr)))
                    setResult(5)
                }
                finish()
            } else {
                toast("请补全信息")
            }
        }

        id_edit_et_part.setOnClickListener {
            val pickerView = CityPickerView.Builder(this)
                    .textSize(18)
                    .title("选择地区")
                    .itemPadding(10)
                    .visibleItemsCount(7)
                    .titleBackgroundColor("#fd6f36")
                    .titleTextColor("#ffffff")
                    .backgroundPop(0x00000000)
                    .confirTextColor("#ffffff")
                    .cancelTextColor("#ffffff")
                    .textColor(Color.parseColor("#000000"))
                    .provinceCyclic(true)
                    .cityCyclic(true)
                    .districtCyclic(true)
                    .onlyShowProvinceAndCity(false)
                    .province("河北省")
                    .city("廊坊市")
                    .district("香河县")
                    .build()
            pickerView.show()
            pickerView.setOnCityItemClickListener(object : CityPickerView.OnCityItemClickListener {
                override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                    id_edit_et_part.setText("${province!!.name} ${city!!.name} ${district!!.name}")
                }

                override fun onCancel() {
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addrDao.onDestroy()
    }

}
