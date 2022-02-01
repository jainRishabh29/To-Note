package com.example.tonote.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tonote.R
import com.example.tonote.util.LocalKeyStorage
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            (R.string.open),
            (R.string.close)
        ){

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.setting -> {
                    Log.d("batao" , "clicked1")
                    true
                }
                R.id.rateUs -> {
                    Log.d("batao" , "clicked2")
                    true
                }
                R.id.Hnotes -> {
                    Log.d("batao" , "clicked3")
                    navController.navigate(R.id.action_mainFragment_to_passwordFragment)
                    drawerLayout.close()
                    true
                }
                R.id.aboutUs -> {
                    Log.d("batao" , "clicked4")
                    true
                }
                R.id.help -> {
                    Log.d("batao" , "clicked5")
                    true
                }
//                R.id.license -> {
//                    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
//                    true
//                }
                else -> true
            }
        }


    }

//    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
//        when (menuItem.itemId) {
//            R.id.about -> {
//
//                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
//                drawerLayout.close()
//
//            }
//            R.id.license ->{
//                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
//
//            }
//            R.id.feedback ->{
//
//                navController.navigate(R.id.action_homeFragment_to_feedbackFragment)
//                drawerLayout.close()
//
//            }
////            else -> Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
//        }
//        return true
//    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}