package com.dicoding.estomihi.githubsearchuser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.dicoding.estomihi.githubsearchuser.model.AlarmReceiver
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    companion object{
        const val PREFS_NAME = "Settings"
        private const val DAILY_REMAINDER = "daily remainder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title=getString(R.string.settings)
        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setSwitch()
        daily_remainder.setOnCheckedChangeListener{ _, isChecked ->
        if(isChecked){
            saveSetting(true)
            alarmReceiver.setDailyAlarm(this, AlarmReceiver.EXTRA_TYPE, "09:00")
        }
            else{
                saveSetting(false)
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEAT)
        }

        }

        btn_language.setOnClickListener{
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun setSwitch(){
        daily_remainder.isChecked =mSharedPreferences.getBoolean(DAILY_REMAINDER, false)
    }

    private fun saveSetting(value: Boolean){
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY_REMAINDER, value)
        editor.apply()
    }
}