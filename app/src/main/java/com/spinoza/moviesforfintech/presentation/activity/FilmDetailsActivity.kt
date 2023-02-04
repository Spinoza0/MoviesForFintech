package com.spinoza.moviesforfintech.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.spinoza.moviesforfintech.databinding.ActivityFilmDetailsBinding
import com.spinoza.moviesforfintech.domain.model.Film

class FilmDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFilmDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra(KEY_FILM)) {
            parseArguments()
            setupScreen()
        } else {
            finish()
        }
    }

    private fun setupScreen() {
        with(film) {
            with(binding) {
                Glide.with(root).load(posterUrl).into(imageViewPoster)
                textViewName.text = nameRu
                textViewDescription.text = description
                textViewGenres.text = genres
                textViewCountries.text = countries
                imageViewArrow.setOnClickListener { finish() }
            }
        }
    }

    private fun parseArguments() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KEY_FILM, Film::class.java)?.let {
                film = it
            }
        } else {
            @Suppress("deprecation")
            film = intent.getParcelableExtra<Film>(KEY_FILM) as Film
        }
    }

    companion object {
        private const val KEY_FILM = "film"

        fun newIntent(context: Context, film: Film) =
            Intent(context, FilmDetailsActivity::class.java).apply {
                putExtra(KEY_FILM, film)
            }
    }
}