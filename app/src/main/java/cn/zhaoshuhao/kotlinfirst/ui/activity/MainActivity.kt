package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.*
import cn.zhaoshuhao.kotlinfirst.contract.AroundPresent
import cn.zhaoshuhao.kotlinfirst.contract.CartPresent
import cn.zhaoshuhao.kotlinfirst.contract.MainPresent
import cn.zhaoshuhao.kotlinfirst.fragment.ARoundFragment
import cn.zhaoshuhao.kotlinfirst.fragment.CartFragment
import cn.zhaoshuhao.kotlinfirst.fragment.MainFragment
import cn.zhaoshuhao.kotlinfirst.fragment.MineFragment
import cn.zhaoshuhao.kotlinfirst.model.bean.WebViewInfo
import cn.zhaoshuhao.kotlinfirst.model.db.KDbHelper
import cn.zhaoshuhao.kotlinfirst.model.db.KSearchDao
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.lljjcoder.citylist.CityListSelectActivity
import com.lljjcoder.citylist.bean.CityInfoBean
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : BaseActivity(), CheckoutToolbar, IRefreshListener {
    private val mTabText = arrayOf("主页", "周边", "购物车", "我的")
    private var mHomeTabText = mTabText[0]

    private val mTabIcon = arrayOf(R.drawable.ic_tab_artists, R.drawable.ic_tab_albums,
            R.drawable.ic_tab_cart, R.drawable.ic_tab_songs)

    private val mTabTarget by lazy {
        val mainFragment = MainFragment()
        val mainPresent = MainPresent(this)
        mainFragment.setPresent(mainPresent)
        mainPresent.setView(mainFragment)

        val aroundFragment = ARoundFragment()
        val aroundPresent = AroundPresent(this)
        aroundFragment.setPresent(aroundPresent)
        aroundPresent.setView(aroundFragment)

        val cartFragment = CartFragment()
        val cartPresent = CartPresent(this)
        cartFragment.setPresent(cartPresent)
        cartPresent.setView(cartFragment)

        arrayOf(mainFragment, aroundFragment,
                cartFragment, MineFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KPermission.requestOfLambda(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                activity = this) { g, d, n ->
            /*针对权限做不同处理*/
        }
        mTabTarget/*利用Kotlin机制初始化数据，此处防止切换时数据丢失*/
        locationClient
        initFragmentUI();
    }

    private val mHelper: KDbHelper?
        get() {
            return KDbHelper.getHelper(this)
        }

    override fun obtainLayoutID(): Int = R.layout.activity_main

    override fun initViews() {
        initBottomNavigationBar()
    }

    override fun initToolbar(): Unit = with(id_toolbar as Toolbar) {
        setSupportActionBar(this)
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.id_main_location -> {
                    doLocation()
                    true
                }
                R.id.id_main_search -> {
                    false
                }
                R.id.id_main_scan -> {
                    KPermission.requestOfLambda(Manifest.permission.CAMERA, activity = this@MainActivity) { g, d, n ->
                        when {
                            g.isNotEmpty() -> {
                                startActivity<ScanActivity>()
                                logd("授权成功")
                            }
                            d.isNotEmpty() -> showTipDialog()
                            n.isNotEmpty() -> gotoSettings()
                        }
                    }
                    true
                }
                R.id.id_main_map -> {
                    val bundle = Bundle()
                    bundle.putSerializable("webinfo", WebViewInfo("高德地图", "http://m.amap.com"))
                    startActivity<WebViewActivity>(bundle)
                    true
                }
                R.id.id_main_delete -> {
                    (mTabTarget[2] as CartFragment).onRemoveData()
                    true
                }
                else -> false
            }
        }
        initDrawer()
    }

    private val locationClient: AMapLocationClient
        get() {
            return AMapLocationClient(applicationContext)
        }

    private fun doLocation() {
        KPermission.requestOfLambda(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, activity = this) { g, d, _ ->
            if (g.isNotEmpty()) {
//                val option: AMapLocationClientOption = getDefaultOption()
//                locationClient.setLocationOption(option)
//                locationClient.setLocationListener { it ->
//                    if (it == null) {
//                        logd("定位失败")
//                    }
//                    if (it?.errorCode == 0) {
//                        toast(it.city)
//                        title = it.city
//                        logd(it.city)
//                    } else {
//                        toast("定位失败")
//                        logd("定位失败")
//                    }
//                }
//                locationClient.startLocation()
                val intent = Intent(this@MainActivity, CityListSelectActivity::class.java)
                intent.putExtra("city", if (mHomeTabText == "主页") null else mHomeTabText)
                startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG)
            }
            if (d.isNotEmpty()) toast("${d.toString()}权限被拒绝")
        }
    }

    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption();
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false;//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000;//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000;//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true;//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = true;//可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = true;//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false;//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan = true; //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = true; //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private fun initDrawer() = with(id_main_drawer as DrawerLayout) {
        val toggle = ActionBarDrawerToggle(this@MainActivity, this, id_toolbar as Toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle.syncState()
        addDrawerListener(toggle)
        initNavigationView()
    }

    private fun initNavigationView() = with(id_main_navigation as NavigationView) {
        setNavigationItemSelectedListener {
            toast("${it.itemId}");
            id_main_drawer.closeDrawer(Gravity.START)
            true
        }
    }

    private fun initBottomNavigationBar() = with(id_main_bottom_navigation) {
        setMode(BottomNavigationBar.MODE_SHIFTING)
        setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        for (i in 0..3) {
            addItem(BottomNavigationItem(mTabIcon[i], mTabText[i]))
        }
        initialise()
        setFirstSelectedPosition(0)
//            selectTab(0)
        setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {
                logd("tab reselected -----> $position")
            }

            override fun onTabUnselected(position: Int) {
                logd("tab unselected -----> $position")
                supportFragmentManager.beginTransaction().hide(mTabTarget[position]).commit()
            }

            override fun onTabSelected(position: Int) {
                logd("tab selected -----> $position")
                mCurrentPage = position
                supportFragmentManager.beginTransaction().show(mTabTarget[mCurrentPage]).commit()
                mTabTarget[mCurrentPage].checkout(this@MainActivity)
            }
        })
    }

    private fun initFragmentUI() {
        mCurrentPage = 0
        with(supportFragmentManager.beginTransaction()) {
            (0..3)
                    .mapNotNull { supportFragmentManager.findFragmentByTag("id$it") }
                    .forEach { remove(it) }
            for (i in 0..3) {
                add(R.id.id_main_fragment_content, mTabTarget[3 - i], "id$i")
                hide(mTabTarget[3 - i])
            }
            show(mTabTarget[mCurrentPage])
            mTabTarget[mCurrentPage].checkout(this@MainActivity)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        logd("currentPage-----> $mCurrentPage")
        mTabTarget[mCurrentPage].checkout(this@MainActivity)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (mCurrentPage) {
            0 -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = true
                menu?.findItem(R.id.id_main_scan)?.isVisible = true
                menu?.findItem(R.id.id_main_location)?.isVisible = true
                menu?.findItem(R.id.id_main_map)?.isVisible = false
                menu?.findItem(R.id.id_main_delete)?.isVisible = false
            }
            1 -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = false
                menu?.findItem(R.id.id_main_scan)?.isVisible = false
                menu?.findItem(R.id.id_main_location)?.isVisible = false
                menu?.findItem(R.id.id_main_map)?.isVisible = true
                menu?.findItem(R.id.id_main_delete)?.isVisible = false
            }
            2 -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = false
                menu?.findItem(R.id.id_main_scan)?.isVisible = false
                menu?.findItem(R.id.id_main_location)?.isVisible = false
                menu?.findItem(R.id.id_main_map)?.isVisible = false
                menu?.findItem(R.id.id_main_delete)?.isVisible = true
            }
            else -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = false
                menu?.findItem(R.id.id_main_scan)?.isVisible = false
                menu?.findItem(R.id.id_main_location)?.isVisible = false
                menu?.findItem(R.id.id_main_map)?.isVisible = false
                menu?.findItem(R.id.id_main_delete)?.isVisible = false
            }
        }
        return true
    }

    override fun obtainMenuRes(): Int {
        return R.menu.main_activity_toolbar
    }

    private val searchDao: KSearchDao
        get() {
            return KSearchDao.get(this)
        }

    override fun initMenuAction(menu: Menu?) {
        val item = menu?.findItem(R.id.id_main_search)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.isIconified = true
        searchView.queryHint = "吃喝玩乐"
        /*获取组件中控件的id，并通过id获得控件对象，修改其属性*/
        val searchTv: SearchView.SearchAutoComplete = searchView!!.findViewById(R.id.search_src_text)
        searchTv.setTextColor(Color.WHITE)
        searchTv.setHintTextColor(Color.WHITE)
        searchTv.threshold = 1 //设置1个字符便可匹配

//        searchDao.insert(Pair("origin", "film"), Pair("data", "陆垚知马俐"),
//                Pair("origin", "film"), Pair("data", "快手枪手快枪手"),
//                Pair("origin", "film"), Pair("data", "惊天大逆转"),
//                Pair("origin", "film"), Pair("data", "大鱼海棠"),
//                Pair("origin", "film"), Pair("data", "寒战2"),
//                Pair("origin", "film"), Pair("data", "忍者神龟2"))

        var cursor: Cursor? = null

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                cursor!!.moveToPosition(position)
                val s = cursor!!.getString(cursor!!.getColumnIndex(cursor!!.getColumnName(2)))
                searchTv?.setText(s)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                logd("submit ${query ?: "empty"}"); return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                logd("text change : ${newText ?: "empty"}")
                if (!TextUtils.isEmpty(newText)) {
                    cursor = searchDao.queryForLike(if (newText?.isEmpty()!!) "" else newText)
                    if (searchView.suggestionsAdapter == null) {
                        val adapter = SimpleCursorAdapter(this@MainActivity, android.R.layout.simple_list_item_1, cursor, arrayOf("data"), intArrayOf(android.R.id.text1))
                        searchView.suggestionsAdapter = adapter
                    } else
                        searchView.suggestionsAdapter.changeCursor(cursor)
                }
                return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == Activity.RESULT_OK) {
                val cityInfoBean: CityInfoBean = data?.extras?.getParcelable<CityInfoBean>("cityinfo")!!
                mHomeTabText = cityInfoBean?.name ?: "主页"
                refreshToolbarTitle(mHomeTabText)
            }
        }
    }

    private var lastTime = 0L

    override fun onBackPressed() {
        if (id_main_drawer.isDrawerOpen(Gravity.START)) {
            id_main_drawer.closeDrawer(Gravity.START)
            return
        }
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastTime < 2000) {
            finish()
        } else {
            lastTime = currentTimeMillis
            toast("再按一次退出")
        }
    }

    override fun onDestroy() {
        searchDao.onDestroy()
        super.onDestroy()
    }

    private var mCurrentPage: Int = 0

    override fun toTarget(fragment: Fragment) {
        when (fragment) {
            is MainFragment -> {
                mCurrentPage = 0
                refreshToolbarTitle(mHomeTabText)
                return
            }
            is ARoundFragment -> mCurrentPage = 1
            is CartFragment -> mCurrentPage = 2
            is MineFragment -> mCurrentPage = 3
        }
        refreshToolbarTitle(mTabText[mCurrentPage])
    }

    private fun refreshToolbarTitle(title: String) {
        supportActionBar?.title = title
        invalidateOptionsMenu()
        logd("应显示的标题和序号 title:$title, page:$mCurrentPage")
    }

    private var isRefreshing = false

    override fun isRefreshing() {
        id_main_bottom_navigation.hide()
        isRefreshing = true
        logd("刷新回调")
    }

    override fun refreshComplete() {
        id_main_bottom_navigation.show()
        isRefreshing = false
        logd("刷新结束")
    }

    /**
     * 引导用户到设置界面打开相机权限
     * */
    private fun gotoSettings() {

    }

    /**
     * 提示并引导用户相机权限的重要性
     * */
    private fun showTipDialog() {

    }
}

interface IRefreshListener {
    fun isRefreshing()

    fun refreshComplete()
}

