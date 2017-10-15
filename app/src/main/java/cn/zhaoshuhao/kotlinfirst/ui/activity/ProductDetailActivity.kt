package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.sharesdk.onekeyshare.OnekeyShare
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.contract.Detail
import cn.zhaoshuhao.kotlinfirst.contract.DetailPresent
import cn.zhaoshuhao.kotlinfirst.model.bean.History
import cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart
import cn.zhaoshuhao.kotlinfirst.model.bean.Star
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.model.db.KCartDao
import cn.zhaoshuhao.kotlinfirst.model.db.KHistoryDao
import cn.zhaoshuhao.kotlinfirst.model.network.BASE_API
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.model.network.entity.ProductDetail
import cn.zhaoshuhao.kotlinfirst.utils.anyToJson
import cn.zhaoshuhao.kotlinfirst.utils.jsonToAny
import cn.zhaoshuhao.kotlinfirst.utils.load
import cn.zhaoshuhao.kotlinfirst.utils.transparentStatusBar
import kotlinx.android.synthetic.main.activity_product_detail.*


class ProductDetailActivity : BaseActivity(), Detail.View {
    private lateinit var present: Detail.Present

    private lateinit var product: GuessYouLike

    private var cartData: ShoppingCart? = null

    private val cartDao: KCartDao
        get() {
            return KCartDao.get(this)
        }


    override fun obtainLayoutID(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        present.addParams(product.goods_id.toInt())
        present.onStart()
    }

    private val historyDao: KHistoryDao
        get() {
            return KHistoryDao.get(this)
        }

    override fun prepareInitUI() {
        transparentStatusBar()
        bindMvp()
        product = intent.extras.getSerializable("product") as GuessYouLike

        val history = History(product.images[0].image, product.product, product.price, "此功能尚在开发中")
        historyDao.insert(Pair("name", history.title), Pair("json", history.toString()))
    }

    private fun bindMvp() {
        setPresent(DetailPresent(this))
        present.setView(this)
    }

    override fun initViews() {
        id_detail_fab_buy.setOnClickListener {
            if (cartData != null) {
                cartData!!.num = (cartData!!.num.toInt() + 1).toString()
                cartDao.updateForAbs("name", product.product, Pair("json", anyToJson(cartData!!)))
                toast("成功添加到购物车")
                logd(cartData.toString())
                return@setOnClickListener
            } else {
                val cursor = cartDao.queryForAbs("name", product.product)
                if (cursor == null || cursor.count == 0) {
                    cartData = ShoppingCart(product.goods_id, product.product,
                            product.images[0].image, product.price,
                            product.value, "1", false)
                    cartDao.insert(Pair("name", product.product), Pair("json", anyToJson(cartData!!)))
                } else {
                    cursor.moveToNext()
                    val json = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)))
                    cartData = jsonToAny(json)
                    cartData!!.num = (cartData!!.num.toInt() + 1).toString()
                    cartDao.updateForAbs("name", product.product, Pair("json", anyToJson(cartData!!)))
                    logd(cartData.toString())
                    cursor?.close()
                }
                toast("成功添加到购物车")
            }
        }
    }

    private lateinit var btnStar: MenuItem

    override fun initToolbar() {
        with(id_detail_toolbar as Toolbar) {
            title = product.product

            setSupportActionBar(this)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.id_detail_share -> {
                        doShare()
                        true
                    }
                    R.id.id_detail_star -> {
                        /*收藏*/
//                        btnStar = it
                        doStar()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private val user: User?
        get() {
            return BmobUser.getCurrentUser(User::class.java)
        }

    private fun doStar() {
        val user = user
        if (user == null) toast("请先登录")
        else {
            if (isStar) {
                val star = Star(user)
                star.objectId = this.star?.objectId
                star?.delete(star?.objectId, object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        logd("${star.toString()} ${star?.user?.mobilePhoneNumber}")
                        if (e == null) {
                            toast("取消收藏成功")
                            changeStarIcon(false)
                        } else toast(e.localizedMessage)
                    }
                })
            } else {
                star = Star(user, product.images[0].image, product.product, product.price, "功能尚在开发中")
                star?.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, e: BmobException?) {
                        if (e == null) {
                            toast("收藏成功")
                            changeStarIcon(true)
                        } else toast(e.localizedMessage)
                    }
                })
            }
        }
    }

    private fun changeStarIcon(b: Boolean) {
        if (b) {
            btnStar.icon = resources.getDrawable(R.drawable.ic_star_24dp, null)
            isStar = true
        } else {
            btnStar.icon = resources.getDrawable(R.drawable.ic_unstar_24dp, null)
            isStar = false
        }
    }

    private fun doShare() {
        val oks = OnekeyShare()
//        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("天天O2O向你推荐：${product.product}");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://${product.images[1].image}");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我向你推荐${product.product}，真的很好哟");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://${product.images[1].image}");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("快来试试这个产品吧！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("仿天天O2O");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    override fun onDestroy() {
        cartDao.onDestroy()
        super.onDestroy()
    }

    override fun setPresent(present: Detail.Present) {
        this.present = present
    }

    override fun loadError() {
        toast("加载失败，请重试"); onBackPressed()
    }

    override fun showDeatilInfo(data: ProductDetail) {
        val result = data.result
        id_detail_iv_image.load(this@ProductDetailActivity, result.images[0].image)
        id_detail_iv_image.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("images", result)
            toActivity<ImageGalleryActivity>(bundle)
        }
        id_detail_tv_title.text = product.title ?: result.title
        id_detail_tv_bought.text = "已售${product.bought.toString()}份"
        id_detail_web_detail.loadDataWithBaseURL(BASE_API, result.details, "text/html", "UTF-8", null)
        id_detail_web_notices.loadDataWithBaseURL(BASE_API, result.notice, "text/html", "UTF-8", null)
        id_detail_tv_price.text = "￥${result.price}"
        id_detail_tv_value.text = "门市价：￥${result.value}"
        id_detail_tv_value.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun obtainMenuRes(): Int = R.menu.product_detail_activity_toolbar

    private var isStar = false
    private var star: Star? = null

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        btnStar = menu?.findItem(R.id.id_detail_star)!!
        if (user == null){
            return super.onPrepareOptionsMenu(menu)
        }
        val query = BmobQuery<Star>()
        query.addWhereEqualTo("title", product.product)
        query.include("user")
        query.findObjects(object : FindListener<Star>() {
            override fun done(res: MutableList<Star>?, e: BmobException?) {
                if (e == null && res?.isNotEmpty()!!) {
                    isStar = true
                    changeStarIcon(true)
                    star = res[0]
                } else
                    logd(e?.localizedMessage!!)
            }
        })
        return super.onPrepareOptionsMenu(menu)
    }
}
