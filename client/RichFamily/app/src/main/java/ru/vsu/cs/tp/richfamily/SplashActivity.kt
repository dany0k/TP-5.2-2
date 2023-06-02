package ru.vsu.cs.tp.richfamily

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.ActivitySplashBinding
import ru.vsu.cs.tp.richfamily.utils.DataBaseHelper
import java.lang.Exception

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tryConnect()
        supportActionBar?.hide()
        binding.reconnectButton.setOnClickListener {
            binding.reconnectButton.visibility = View.GONE
            binding.serverOfflineTv.visibility = View.GONE
            tryConnect()
        }
    }

    private fun tryConnect() {
        val mDelay: Long = 5000
        val db = DataBaseHelper(applicationContext, null)
        var cursor = db.getFlag()!!
        cursor.moveToFirst()
        val flag = cursor.getColumnIndex(DataBaseHelper.FLAG)
        try {
            cursor.getInt(flag)
        } catch (e: Exception) {
            db.setFlag(0)
            cursor = db.getFlag()!!
            cursor.moveToFirst()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                if (App.checkServerConnection()) {
                    if (flag == -1 || cursor.getInt(flag) == 0) {
                        val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else run {
                    this@SplashActivity.runOnUiThread {
                        binding.reconnectButton.visibility = View.VISIBLE
                        binding.serverOfflineTv.visibility = View.VISIBLE
                    }
                }
            }
        }, mDelay)
    }
}