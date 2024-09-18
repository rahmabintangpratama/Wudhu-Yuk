package com.kiiis.wudhuyuk.ui.learn

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kiiis.wudhuyuk.databinding.ActivityLearnBinding
import com.kiiis.wudhuyuk.ui.batal.BatalActivity
import com.kiiis.wudhuyuk.ui.doa.DoaActivity
import com.kiiis.wudhuyuk.ui.meaning.MeaningActivity
import com.kiiis.wudhuyuk.ui.niat.NiatActivity
import com.kiiis.wudhuyuk.ui.procedure.ProcedureActivity
import com.kiiis.wudhuyuk.ui.references.ReferencesActivity
import com.kiiis.wudhuyuk.ui.rukun.RukunActivity
import com.kiiis.wudhuyuk.ui.sunnah.SunnahActivity
import com.kiiis.wudhuyuk.ui.syarat.SyaratActivity

class LearnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.ivPengertianWudhu.setOnClickListener {
            startActivity(Intent(this, MeaningActivity::class.java))
        }

        binding.ivRukunWudhu.setOnClickListener {
            startActivity(Intent(this, RukunActivity::class.java))
        }

        binding.ivSunnahWudhu.setOnClickListener {
            startActivity(Intent(this, SunnahActivity::class.java))
        }

        binding.ivSyaratWudhu.setOnClickListener {
            startActivity(Intent(this, SyaratActivity::class.java))
        }

        binding.ivNiatWudhu.setOnClickListener {
            startActivity(Intent(this, NiatActivity::class.java))
        }

        binding.ivTataCaraWudhu.setOnClickListener {
            startActivity(Intent(this, ProcedureActivity::class.java))
        }

        binding.ivDoaWudhu.setOnClickListener {
            startActivity(Intent(this, DoaActivity::class.java))
        }

        binding.ivBatalWudhu.setOnClickListener {
            startActivity(Intent(this, BatalActivity::class.java))
        }

        binding.ivDaftarPustaka.setOnClickListener {
            startActivity(Intent(this, ReferencesActivity::class.java))
        }
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

    private companion object {
        const val SUN_DURATION = 25000
        const val CLOUD_DURATION = 6000
    }
}