package com.kiiis.wudhuyuk.ui.doa

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivityDoaBinding
import com.kiiis.wudhuyuk.settings.FontScaleSetting

class DoaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoaBinding
    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var mediaPlayerDoa: MediaPlayer? = null
    private var mediaPlayerBacksound: MediaPlayer? = null
    private var isDoaPlaying = false

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontScaleSetting.updateBaseContextLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontScaleSetting.resetFontScale(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAudioPlayers()
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

        ObjectAnimator.ofPropertyValuesHolder(binding.ivBack, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupAudioPlayers() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        clickSoundId = soundPool.load(this, R.raw.aud_click, 1)
        mediaPlayerDoa = MediaPlayer.create(this, R.raw.aud_doa_wudhu)
        mediaPlayerBacksound = MediaPlayer.create(this, R.raw.aud_home)

        mediaPlayerDoa?.setOnCompletionListener {
            stopDoaAudio()
            startBacksoundAudio()
        }

        mediaPlayerBacksound?.isLooping = true
        startBacksoundAudio()
    }

    private fun setupClickListeners() {
        binding.btnPlay.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            if (isDoaPlaying) {
                stopDoaAudio()
                startBacksoundAudio()
            } else {
                startDoaAudio()
            }
        }

        binding.ivBack.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            finish()
        }
    }

    private fun startDoaAudio() {
        mediaPlayerBacksound?.pause()
        mediaPlayerDoa?.start()
        isDoaPlaying = true
        binding.btnPlay.text = getString(R.string.stop)
    }

    private fun stopDoaAudio() {
        mediaPlayerDoa?.pause()
        mediaPlayerDoa?.seekTo(0)
        isDoaPlaying = false
        binding.btnPlay.text = getString(R.string.play)
    }

    private fun startBacksoundAudio() {
        if (mediaPlayerBacksound?.isPlaying == false) {
            mediaPlayerBacksound?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerBacksound?.pause()
        if (isDoaPlaying) {
            mediaPlayerDoa?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isDoaPlaying) {
            startBacksoundAudio()
        } else {
            mediaPlayerDoa?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerDoa?.release()
        mediaPlayerDoa = null
        mediaPlayerBacksound?.release()
        mediaPlayerBacksound = null
    }

    private companion object {
        val scaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.15f)
        val scaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.15f)
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
        const val WOOD_DURATION = 1000
    }
}