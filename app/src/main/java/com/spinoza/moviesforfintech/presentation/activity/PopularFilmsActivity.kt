package com.spinoza.moviesforfintech.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.ActivityPopularFilmsBinding
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.presentation.fragment.FilmDetailsFragment
import com.spinoza.moviesforfintech.presentation.fragment.PopularFilmsFragment
import com.spinoza.moviesforfintech.presentation.utils.*

class PopularFilmsActivity : AppCompatActivity(), OnFragmentSavedPositionListener,
    OnFragmentFilmListener {

    private lateinit var savedPosition: SavedPosition
    private lateinit var savedFilm: Film

    private val binding: ActivityPopularFilmsBinding by lazy {
        ActivityPopularFilmsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        savedPosition = getSavedPositionFromBundle(savedInstanceState)

        val fragment: Fragment
        if (savedPosition.openDetails) {
            savedFilm = getFilmFromBundle(savedInstanceState)
            fragment = FilmDetailsFragment.newInstance(savedFilm, savedPosition)
        } else {
            fragment = PopularFilmsFragment.newInstance(savedPosition)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun saveFilm(film: Film) {
        this.savedFilm = film
    }

    override fun savePosition(savedPosition: SavedPosition) {
        this.savedPosition = savedPosition
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SAVED_POSITION, savedPosition)
        if(savedPosition.openDetails) {
            outState.putParcelable(KEY_SAVED_FILM, savedFilm)
        }
    }
}