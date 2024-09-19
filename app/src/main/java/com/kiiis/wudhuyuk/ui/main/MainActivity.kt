package com.kiiis.wudhuyuk.ui.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivityMainBinding
import com.kiiis.wudhuyuk.settings.FontScaleSetting
import com.kiiis.wudhuyuk.ui.about.AboutActivity
import com.kiiis.wudhuyuk.ui.game.GameActivity
import com.kiiis.wudhuyuk.ui.learn.LearnActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000
    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontScaleSetting.updateBaseContextLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontScaleSetting.resetFontScale(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        playAudio()
        setupClickListeners()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivSun, View.ROTATION, 0f, 360f).apply {
            duration = SUN_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }.start()

        ObjectAnimator.ofFloat(binding.ivCloud1, View.TRANSLATION_X, -40f, 40f).apply {
            duration = CLOUD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.ivCloud2, View.TRANSLATION_X, -40f, 40f).apply {
            duration = CLOUD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.ivCloud3, View.TRANSLATION_X, 40f, -40f).apply {
            duration = CLOUD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.ivCloud4, View.TRANSLATION_X, 40f, -40f).apply {
            duration = CLOUD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.ivBelajar, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.ivBermain, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.ivTentang, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun playAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        clickSoundId = soundPool.load(this, R.raw.aud_click, 1)
        mediaPlayer = MediaPlayer.create(this, R.raw.aud_home)
        mediaPlayer.isLooping = true

        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun setupClickListeners() {

        binding.ivBelajar.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            startActivity(Intent(this, LearnActivity::class.java))
        }

        binding.ivBermain.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.ivTentang.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            startActivity(Intent(this, AboutActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {
                    finishAffinity()
                    exitProcess(0)
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.exit), Toast.LENGTH_SHORT)
                        .show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        soundPool.release()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private companion object {
        val scaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.15f)
        val scaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.15f)
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
        const val WOOD_DURATION = 1000
    }
}