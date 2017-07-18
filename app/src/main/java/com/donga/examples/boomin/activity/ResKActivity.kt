package com.donga.examples.boomin.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.donga.examples.boomin.AppendLog
import com.donga.examples.boomin.R
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_res.*
import kotlinx.android.synthetic.main.content_res.*
import java.text.SimpleDateFormat
import java.util.*

class ResKActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var log = AppendLog()

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar_res)

        Log.i("Kotlin Activity_RESK","Success!!!")

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout_res, toolbar_res, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout_res.setDrawerListener(toggle)
        toggle.syncState()

        nav_view_res.setNavigationItemSelectedListener(this)

        val msimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentTime = Date()
        val now = msimpleDateFormat.format(currentTime)


        getMeal(now)

        date_text.text = now

        pre_res.setOnClickListener{
            count -= 1

            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, count) // +1은 내일

            val date = SimpleDateFormat("yyyy-MM-dd")
            val pre = date.format(cal.time)
            getMeal(pre)
            date_text.text = pre
        }
        next_res.setOnClickListener{
            count += 1

            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, count) // +1은 내일

            val date = SimpleDateFormat("yyyy-MM-dd")
            val next = date.format(cal.time)

            getMeal(next)
            date_text.text = next
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getMeal(day:String) {
        FuelManager.instance.basePath = "http://www.dongaboomin.xyz:3000"
        FuelManager.instance.baseParams = listOf("date" to day)
        "/meal".httpGet().responseJson { _, _, result ->

            result.fold({ d ->
                //make a GET to http://httpbin.org/get and do something with response
                val result_object = d.obj()
                val result_body =  result_object.getJSONObject("result_body")

                val sdkVersion = Build.VERSION.SDK_INT

                if (sdkVersion >= 24) {
                    guk.text = Html.fromHtml(result_body.getString("inter"), Html.FROM_HTML_MODE_COMPACT)
                    guk.movementMethod = LinkMovementMethod.getInstance()

                    bumin.text = Html.fromHtml(result_body.getString("bumin_kyo"), Html.FROM_HTML_MODE_COMPACT)
                    bumin.movementMethod = LinkMovementMethod.getInstance()

                    gang.text =  Html.fromHtml(result_body.getString("gang"), Html.FROM_HTML_MODE_COMPACT)
                    gang.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    guk.text = Html.fromHtml(result_body.getString("inter"))
                    guk.movementMethod = LinkMovementMethod.getInstance()

                    bumin.text = Html.fromHtml(result_body.getString("bumin_kyo"))
                    bumin.movementMethod = LinkMovementMethod.getInstance()

                    gang.text =  Html.fromHtml(result_body.getString("gang"))
                    gang.movementMethod = LinkMovementMethod.getInstance()
                }

                Log.i("Result",result_body.getString("inter"))
            }, { _ ->
                Log.e("ERROR","데이터 통신 불가")
            })

        }
    }


    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout_res) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val intent = Intent(baseContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_res) {
            val intent = Intent(applicationContext, ResKActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_room) {
            val intent = Intent(applicationContext, RoomActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_pro) {
            val intent = Intent(applicationContext, ProActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_stu) {
            val intent = Intent(applicationContext, StudentActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_empty) {
            val intent = Intent(applicationContext, EmptyActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_wisper) {
            val intent = Intent(applicationContext, WisperActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_site) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.donga.ac.kr"))
            startActivity(intent)
        } else if (id == R.id.nav_noti) {
            val intent = Intent(applicationContext, NoticeActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_change) {
            val intent = Intent(applicationContext, ChangeActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_help) {
            val intent = Intent(applicationContext, HelpActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_logout) {
            val sharedPreferences = getSharedPreferences(resources.getString(R.string.SFLAG), Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } else if (id == R.id.nav_manage) {
            val intent = Intent(applicationContext, ManageLoginActivity::class.java)
            startActivity(intent)
        }

        val drawer = findViewById(R.id.drawer_layout_res) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}