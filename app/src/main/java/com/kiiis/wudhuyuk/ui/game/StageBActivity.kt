package com.kiiis.wudhuyuk.ui.game

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivityStageBBinding
import com.kiiis.wudhuyuk.ui.main.MainActivity
import kotlin.random.Random

class StageBActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStageBBinding
    private lateinit var faucets: List<ImageView>
    private var score = 0
    private var isGameRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private var previousFaucet: ImageView? = null
    private var timeLeft = 30
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var clickSoundId: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private var scoreA = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStageBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreA = intent.getIntExtra("scoreA", 0)

        playAnimation()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        soundId = soundPool.load(this, R.raw.aud_faucet_clicked, 1)
        clickSoundId = soundPool.load(this, R.raw.aud_click, 1)

        faucets = listOf(
            binding.btnFaucet1, binding.btnFaucet2, binding.btnFaucet3,
            binding.btnFaucet4, binding.btnFaucet5, binding.btnFaucet6,
            binding.btnFaucet7, binding.btnFaucet8, binding.btnFaucet9
        )

        mediaPlayer = MediaPlayer.create(this, R.raw.aud_game)
        mediaPlayer.isLooping = true

        updateScoreDisplay()
        updateTimeLeftDisplay()
        startGame()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })
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
    }

    private fun startGame() {
        isGameRunning = true
        score = 0
        timeLeft = 30
        updateScoreDisplay()
        updateTimeLeftDisplay()
        showRandomFaucet()
        startTimer()

        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun startTimer() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                timeLeft--
                updateTimeLeftDisplay()
                if (timeLeft > 0 && isGameRunning) {
                    handler.postDelayed(this, 1000)
                } else {
                    endGame()
                }
            }
        }, 1000)
    }

    private fun showRandomFaucet() {
        if (!isGameRunning) return

        val randomFaucet = getRandomFaucet()
        randomFaucet.setImageResource(R.drawable.ic_open_faucet)
        randomFaucet.visibility = View.VISIBLE

        randomFaucet.setOnClickListener {
            if (randomFaucet.visibility == View.VISIBLE && isGameRunning) {
                hitFaucet(randomFaucet)
            }
        }

        val randomDuration = Random.nextLong(900, 1200)
        handler.postDelayed({
            if (randomFaucet.visibility == View.VISIBLE) {
                randomFaucet.visibility = View.GONE
            }
            if (isGameRunning) {
                showRandomFaucet()
            }
        }, randomDuration)
    }

    private fun getRandomFaucet(): ImageView {
        var randomFaucet: ImageView
        do {
            randomFaucet = faucets[Random.nextInt(faucets.size)]
        } while (randomFaucet == previousFaucet)

        previousFaucet?.visibility = View.GONE
        previousFaucet = randomFaucet
        return randomFaucet
    }

    private fun hitFaucet(faucet: ImageView) {
        faucet.setOnClickListener(null)

        faucet.setImageResource(R.drawable.ic_close_faucet)
        score++
        updateScoreDisplay()

        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)

        handler.postDelayed({
            faucet.visibility = View.GONE
            faucet.setImageResource(R.drawable.ic_open_faucet)
        }, 200)
    }

    private fun updateScoreDisplay() {
        binding.tvScoreValue.text = score.toString()
    }

    private fun updateTimeLeftDisplay() {
        binding.tvTimeLeftValue.text = timeLeft.toString()
    }

    private fun endGame() {
        isGameRunning = false
        handler.removeCallbacksAndMessages(null)
        faucets.forEach { it.visibility = View.VISIBLE }

        mediaPlayer.pause()
        mediaPlayer.seekTo(0)

        binding.btnNextStage.visibility = View.VISIBLE
        binding.btnNextStage.setOnClickListener {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
            val intent = Intent(this, StageCActivity::class.java)
            intent.putExtra("scoreA", scoreA)
            intent.putExtra("scoreB", score)
            startActivity(intent)
            finish()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.exit_dialog_title))
            .setMessage(getString(R.string.exit_dialog_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
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
        isGameRunning = false
        handler.removeCallbacksAndMessages(null)

        soundPool.release()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private companion object {
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
    }
}