package com.example.navigationdrawer.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.navigationdrawer.R
import com.example.navigationdrawer.database.userSharedPreference
import com.example.navigationdrawer.database.userSharedPreference.email
import com.example.navigationdrawer.database.userSharedPreference.userName
import com.example.navigationdrawer.ui.fragment.NotesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    val CUSTOM_PREF_NAME = "User_data"
    private var nav_view: NavigationView?=null
    private var nav_view_bottom:BottomNavigationView?=null
    private var drawer_layout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var checkValueName: String? = null
    private var checkValueMail: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout,toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()
         replaceFragments(NotesFragment(),"Notes")

        nav_view?.setNavigationItemSelectedListener {
            it.isChecked = true
            // FRAGMENTS
            when (it.itemId) {
                R.id.nav_profile -> startNewActivity()
                R.id.nav_note -> startNewNotesActivity()
            }
            true
        }

        nav_view_bottom?.setOnNavigationItemSelectedListener {
            it.isChecked = true
            // FRAGMENTS
            when (it.itemId) {

                R.id.buttonBelow->startNewNotesActivity()
            }
            true
        }
        // shared preference
        val prefs = userSharedPreference.customPreference(this, CUSTOM_PREF_NAME)
        checkValueMail=prefs.email
        checkValueName=prefs.userName
        Toast.makeText(baseContext, checkValueMail + "" + checkValueName, Toast.LENGTH_SHORT).show()
        val  headerView: View? = nav_view?.getHeaderView(0)
        val navUsername:TextView? = headerView?.findViewById(R.id.userNameHeader)
        if (navUsername != null) {
            navUsername.setText(checkValueName)
        }
    }

    private fun startNewActivity(){
        val intent = Intent(this, SignupActivity::class.java)
        intent.putExtra("isFromMain",true)
        startActivity(intent)

    }
    private fun startNewNotesActivity(){
        val intent = Intent(this, notesDetailActivity::class.java)
        intent.putExtra("isFromMain",true)
        startActivity(intent)
    }
    private fun setToolbar()
    {
        drawer_layout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.layout_toolbar)
        nav_view = findViewById(R.id.nav_view)
        nav_view_bottom=findViewById(R.id.bottom_nav_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
   private fun replaceFragments(fragment: Fragment,title:String) {

       val fragmentManager=supportFragmentManager
       val fragmentTransaction=fragmentManager.beginTransaction()
       fragmentTransaction.replace(R.id.fragmentContainer,fragment)
       fragmentTransaction.commit()
       drawer_layout?.closeDrawers()
       setTitle(title)
   }

}