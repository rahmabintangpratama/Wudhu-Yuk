package com.kiiis.wudhuyuk.ui.sunnah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kiiis.wudhuyuk.databinding.ActivitySunnahBinding

class SunnahActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySunnahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySunnahBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}