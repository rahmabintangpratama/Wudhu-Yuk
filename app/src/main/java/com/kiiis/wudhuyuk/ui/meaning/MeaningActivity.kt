package com.kiiis.wudhuyuk.ui.meaning

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kiiis.wudhuyuk.databinding.ActivityMeaningBinding

class MeaningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeaningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeaningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        binding.ivBack.setOnClickListener { finish() }
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

    private companion object {
        val scaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.15f)
        val scaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.15f)
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
        const val WOOD_DURATION = 1000
    }
}