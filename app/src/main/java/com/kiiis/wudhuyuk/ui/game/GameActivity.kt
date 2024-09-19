package com.kiiis.wudhuyuk.ui.game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivityGameBinding
import com.kiiis.wudhuyuk.settings.FontScaleSetting

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var startSoundId: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private val animationDuration: Long = 1000
    private val transitionDelay: Long = 300

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontScaleSetting.updateBaseContextLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        FontScaleSetting.resetFontScale(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSoundPool()
        setupMediaPlayer()
        setupViews()
        setupListeners()
        playBackgroundAnimations()
    }

    private fun setupSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        clickSoundId = soundPool.load(this, R.raw.aud_click, 1)
        startSoundId = soundPool.load(this, R.raw.aud_click, 1)
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.aud_home)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    private fun setupViews() {
        binding.ivCharacter.visibility = View.GONE
        binding.cardViewNarration1.visibility = View.GONE
        binding.cardViewNarration2.visibility = View.GONE
        binding.cardViewNarration3.visibility = View.GONE
        binding.cardViewNarration4.visibility = View.GONE

        showMessage(1)
    }

    private fun setupListeners() {
        binding.tvNext1.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            disableNextButtons()
            showMessage(2)
        }

        binding.tvNext2.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            disableNextButtons()
            showMessage(3)
        }

        binding.tvNext3.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            disableNextButtons()
            showMessage(4)
        }

        binding.tvNext4.setOnClickListener {
            soundPool.play(startSoundId, 1f, 1f, 1, 0, 1f)
            disableNextButtons()
            startActivity(Intent(this, StageAActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            finish()
        }
    }

    private fun disableNextButtons() {
        binding.tvNext1.isClickable = false
        binding.tvNext2.isClickable = false
        binding.tvNext3.isClickable = false
        binding.tvNext4.isClickable = false
    }

    private fun enableNextButton(messageNumber: Int) {
        when (messageNumber) {
            1 -> binding.tvNext1.isClickable = true
            2 -> binding.tvNext2.isClickable = true
            3 -> binding.tvNext3.isClickable = true
            4 -> binding.tvNext4.isClickable = true
        }
    }

    private fun showMessage(messageNumber: Int) {
        if (messageNumber > 1) {
            hideMessage(messageNumber - 1)
            Handler(Looper.getMainLooper()).postDelayed({
                showNewMessage(messageNumber)
            }, animationDuration + transitionDelay)
        } else {
            showNewMessage(messageNumber)
        }
    }

    private fun showNewMessage(messageNumber: Int) {
        val character = binding.ivCharacter
        val cardView = when (messageNumber) {
            1 -> binding.cardViewNarration1
            2 -> binding.cardViewNarration2
            3 -> binding.cardViewNarration3
            4 -> binding.cardViewNarration4
            else -> return
        }

        character.visibility = View.VISIBLE
        character.alpha = 0f
        cardView.visibility = View.VISIBLE
        cardView.alpha = 0f

        val fadeIn = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(character, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f)
            )
            duration = animationDuration
        }

        fadeIn.start()

        Handler(Looper.getMainLooper()).postDelayed({
            enableNextButton(messageNumber)
        }, animationDuration)
    }

    private fun hideMessage(messageNumber: Int) {
        val character = binding.ivCharacter
        val cardView = when (messageNumber) {
            1 -> binding.cardViewNarration1
            2 -> binding.cardViewNarration2
            3 -> binding.cardViewNarration3
            4 -> binding.cardViewNarration4
            else -> return
        }

        val fadeOut = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(character, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(cardView, "alpha", 1f, 0f)
            )
            duration = animationDuration
        }

        fadeOut.start()
    }

    private fun playBackgroundAnimations() {
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

        ObjectAnimator.ofPropertyValuesHolder(binding.tvNext1, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.tvNext2, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.tvNext3, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofPropertyValuesHolder(binding.tvNext4, scaleX, scaleY).apply {
            duration = WOOD_DURATION.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        mediaPlayer.release()
    }

    companion object {
        private val scaleX: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.15f)
        private val scaleY: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.15f)
        private const val SUN_DURATION = 25000
        private const val CLOUD_DURATION = 6000
        private const val WOOD_DURATION = 1000
    }
}