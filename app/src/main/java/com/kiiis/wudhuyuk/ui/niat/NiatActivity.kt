package com.kiiis.wudhuyuk.ui.niat

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
import com.kiiis.wudhuyuk.databinding.ActivityNiatBinding
import com.kiiis.wudhuyuk.settings.FontScaleSetting

class NiatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNiatBinding
    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var mediaPlayerNiat: MediaPlayer? = null
    private var mediaPlayerBacksound: MediaPlayer? = null
    private var isNiatPlaying = false

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontScaleSetting.updateBaseContextLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontScaleSetting.resetFontScale(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNiatBinding.inflate(layoutInflater)
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
        mediaPlayerNiat = MediaPlayer.create(this, R.raw.aud_niat_wudhu)
        mediaPlayerBacksound = MediaPlayer.create(this, R.raw.aud_home)

        mediaPlayerNiat?.setOnCompletionListener {
            stopNiatAudio()
            startBacksoundAudio()
        }

        mediaPlayerBacksound?.isLooping = true
        startBacksoundAudio()
    }

    private fun setupClickListeners() {
        binding.btnPlay.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            if (isNiatPlaying) {
                stopNiatAudio()
                startBacksoundAudio()
            } else {
                startNiatAudio()
            }
        }

        binding.ivBack.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            finish()
        }
    }

    private fun startNiatAudio() {
        mediaPlayerBacksound?.pause()
        mediaPlayerNiat?.start()
        isNiatPlaying = true
        binding.btnPlay.text = getString(R.string.stop)
    }

    private fun stopNiatAudio() {
        mediaPlayerNiat?.pause()
        mediaPlayerNiat?.seekTo(0)
        isNiatPlaying = false
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
        if (isNiatPlaying) {
            mediaPlayerNiat?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isNiatPlaying) {
            startBacksoundAudio()
        } else {
            mediaPlayerNiat?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerNiat?.release()
        mediaPlayerNiat = null
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