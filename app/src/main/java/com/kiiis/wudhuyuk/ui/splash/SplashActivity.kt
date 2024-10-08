package com.kiiis.wudhuyuk.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivitySplashBinding
import com.kiiis.wudhuyuk.settings.FontScaleSetting
import com.kiiis.wudhuyuk.ui.main.MainActivity
import kotlin.system.exitProcess

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontScaleSetting.updateBaseContextLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontScaleSetting.resetFontScale(this)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {
                    finishAffinity()
                    exitProcess(0)
                } else {
                    Toast.makeText(
                        this@SplashActivity,
                        getString(R.string.exit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            showUnsupportedVersionDialog()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                goToMainActivity()
            }, SPLASH_DELAY)
        }
    }

    private fun showUnsupportedVersionDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.unsupported_android_dialog_title))
            .setMessage(getString(R.string.unsupported_android_dialog_message))
            .setCancelable(false)
            .create()

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finishAffinity()
            exitProcess(0)
        }, CLOSE_DELAY)
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private companion object {
        private const val SPLASH_DELAY = 3000L
        private const val CLOSE_DELAY = 5000L
    }
}