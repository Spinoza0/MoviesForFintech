package com.spinoza.moviesforfintech.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.ActivityPopularFilmsBinding
import com.spinoza.moviesforfintech.domain.repository.SourceType
import com.spinoza.moviesforfintech.presentation.fragment.PopularFilmsFragment
import com.spinoza.moviesforfintech.presentation.utils.SOURCE_TYPE
import com.spinoza.moviesforfintech.presentation.utils.getSourceTypeFromBundle

class PopularFilmsActivity : AppCompatActivity(), OnFragmentSendDataListener {

    private lateinit var sourceType: SourceType

    private val binding: ActivityPopularFilmsBinding by lazy {
        ActivityPopularFilmsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sourceType = parseArguments(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PopularFilmsFragment.newInstance(sourceType))
            .commit()
    }

    override fun invoke(sourceType: SourceType) {
        this.sourceType = sourceType
    }

    private fun parseArguments(savedInstanceState: Bundle?) =
        getSourceTypeFromBundle(savedInstanceState)


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SOURCE_TYPE, sourceType)
    }
}