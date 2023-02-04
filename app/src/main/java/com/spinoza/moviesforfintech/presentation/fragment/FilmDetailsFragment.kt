package com.spinoza.moviesforfintech.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.spinoza.moviesforfintech.databinding.FragmentFilmDetailsBinding
import com.spinoza.moviesforfintech.domain.model.Film

class FilmDetailsFragment : Fragment() {

    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding: FragmentFilmDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentFilmDetailsBinding == null")

    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreen()
    }

    private fun setupScreen() {
        with(film) {
            with(binding) {
                Glide.with(root).load(posterUrl).into(imageViewPoster)
                textViewName.text = nameRu
                textViewDescription.text = description
                textViewGenres.text = genres
                textViewCountries.text = countries
            }
        }
    }

    private fun parseArguments() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_FILM, Film::class.java)?.let {
                film = it
            }
        } else {
            @Suppress("deprecation")
            requireArguments().getParcelable<Film>(KEY_FILM)?.let {
                film = it
            }
        }
    }

    companion object {
        private const val KEY_FILM = "film"
        fun newInstance(film: Film): FilmDetailsFragment {
            return FilmDetailsFragment().apply {
                arguments = Bundle().apply { putParcelable(KEY_FILM, film) }
            }
        }
    }
}