package com.spinoza.moviesforfintech.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.ActivityPopularFilmsBinding
import com.spinoza.moviesforfintech.presentation.fragment.PopularFilmsFragment

class PopularFilmsActivity : AppCompatActivity() {

    private val binding: ActivityPopularFilmsBinding by lazy {
        ActivityPopularFilmsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PopularFilmsFragment.newInstance())
            .commit()
    }
}