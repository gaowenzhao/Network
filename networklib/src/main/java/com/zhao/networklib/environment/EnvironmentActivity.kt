package com.zhao.networklib.environment

import android.app.Application
import android.os.Bundle
import android.os.Process
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.zhao.networklib.R

class EnvironmentActivity : AppCompatActivity() {
    companion object {
        const val NETWORK_ENVIRONMENT_PREF_KEY: String = "network_environment_type"
        var sCurrentNetworkEnvironment: String = ""
        @JvmStatic
        fun isOfficialEnvironment(application: Application?): Boolean {
            val prefs = PreferenceManager.getDefaultSharedPreferences(application)
            val environment = prefs.getString(Companion.NETWORK_ENVIRONMENT_PREF_KEY, "1")
            return "1".equals(environment, ignoreCase = true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_environment)
        supportFragmentManager.beginTransaction().replace(R.id.content, MyPreferenceFragment()).commit()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        sCurrentNetworkEnvironment = prefs.getString(NETWORK_ENVIRONMENT_PREF_KEY, "1")!!
    }

    class MyPreferenceFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
            addPreferencesFromResource(R.xml.environment_preference)
            findPreference<Preference>(NETWORK_ENVIRONMENT_PREF_KEY)!!.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
            if (!sCurrentNetworkEnvironment.equals(o.toString(), ignoreCase = true)) {
                Toast.makeText(context, "您已经更改了网络环境，再您退出当前页面的时候APP将会重启切换环境！", Toast.LENGTH_SHORT).show()
            }
            return true
        }
    }

    override fun onBackPressed() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val newValue = prefs.getString(NETWORK_ENVIRONMENT_PREF_KEY, "1")
        if (!sCurrentNetworkEnvironment.equals(newValue, ignoreCase = true)) {
            Process.killProcess(Process.myPid())
        } else {
            finish()
        }
    }
}