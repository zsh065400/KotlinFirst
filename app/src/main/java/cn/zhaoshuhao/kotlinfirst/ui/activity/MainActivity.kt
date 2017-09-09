package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.startActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.contract.MainPresent
import cn.zhaoshuhao.kotlinfirst.fragment.*
import cn.zhaoshuhao.kotlinfirst.model.db.KDbHelper
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.lljjcoder.citylist.CityListSelectActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*



class MainActivity : BaseActivity(), CheckoutToolbar {
    private val mTabText = arrayOf("主页", "周边", "已购", "我的")

    private val mTabIcon = arrayOf(R.drawable.ic_tab_artists, R.drawable.ic_tab_albums,
            R.drawable.ic_tab_cart, R.drawable.ic_tab_songs)

    private val mTabTarget by lazy {
        val mainFragment = MainFragment()
        val mainPresent = MainPresent(this)
        mainFragment.setPresent(mainPresent)
        mainPresent.setView(mainFragment)
        arrayOf(mainFragment, ARoundFragment(),
                MineFragment(), MoreFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KPermission.requestOfLambda(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                activity = this) { g, d, n ->
            /*针对权限做不同处理*/
        }
        mTabTarget
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
                    //TODO 点击地图按钮进行相应操作
                    false
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
            toast("定位权限回调")
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

    var mCurIndex = 0

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
            override fun onTabReselected(position: Int) {}

            override fun onTabUnselected(position: Int) {
                logd("tab unselected -----> $position")
                supportFragmentManager.beginTransaction().hide(mTabTarget[position]).commit()
            }

            override fun onTabSelected(position: Int) {
                logd("tab selected -----> $position")
                if (mCurIndex == position) return
                mCurIndex = position
                supportFragmentManager.beginTransaction().show(mTabTarget[mCurIndex]).commit()
                mTabTarget[mCurIndex].checkout(this@MainActivity)
            }
        })
    }

    private fun initFragmentUI() {
        mCurIndex = 0
        with(supportFragmentManager.beginTransaction()) {
            for (i in 0..3) {
                add(R.id.id_main_fragment_content, mTabTarget[3 - i], "id$i")
                hide(mTabTarget[3 - i])
            }
            show(mTabTarget[0])
            commit()
            mTabTarget[0].checkout(this@MainActivity)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (mCurrentPage) {
            0 -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = true
                menu?.findItem(R.id.id_main_scan)?.isVisible = true
                menu?.findItem(R.id.id_main_location)?.isVisible = true
                menu?.findItem(R.id.id_main_map)?.isVisible = false
            }
            else -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = false
                menu?.findItem(R.id.id_main_scan)?.isVisible = false
                menu?.findItem(R.id.id_main_location)?.isVisible = false
                menu?.findItem(R.id.id_main_map)?.isVisible = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun obtainMenuRes(): Int {
        return R.menu.main_activity_toolbar
    }

    private val database: SQLiteDatabase
        get() {
            return mHelper?.readableDatabase!!
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

        val readableDatabase = database
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
                    cursor = readableDatabase.query(mHelper!!.table_search, null, "data like ?", arrayOf("%$newText%"), null, null, null)
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

    override fun onBackPressed() {
        if (id_main_drawer.isDrawerOpen(Gravity.START)) {
            id_main_drawer.closeDrawer(Gravity.START)
            return
        }
        finish()
        super.onBackPressed()
    }

    override fun onDestroy() {
        if (database.isOpen)
            database.close()
        super.onDestroy()
    }

    private var mCurrentPage: Int = 0

    override fun toTarget(fragment: Fragment) {
        when (fragment) {
            is MainFragment -> mCurrentPage = 0
            is ARoundFragment -> mCurrentPage = 1
            is MineFragment -> mCurrentPage = 2
            is MoreFragment -> mCurrentPage = 3

        }
        title = mTabText[mCurrentPage]
        invalidateOptionsMenu()
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



