package com.kiiis.wudhuyuk.ui.doa

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kiiis.wudhuyuk.R
import com.kiiis.wudhuyuk.databinding.ActivityDoaBinding

class DoaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoaBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAudioPlayer()
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

    private fun setupAudioPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.aud_doa_wudhu)
        mediaPlayer?.setOnCompletionListener {
            stopAudio()
        }

        binding.btnPlay.setOnClickListener {
            if (isPlaying) {
                stopAudio()
            } else {
                startAudio()
            }
        }
    }

    private fun startAudio() {
        mediaPlayer?.start()
        isPlaying = true
        binding.btnPlay.text = getString(R.string.stop)
    }

    private fun stopAudio() {
        mediaPlayer?.pause()
        mediaPlayer?.seekTo(0)
        isPlaying = false
        binding.btnPlay.text = getString(R.string.play)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private companion object {
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
    }
}