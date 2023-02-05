package com.spinoza.moviesforfintech.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.FragmentFilmDetailsBinding
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.presentation.activity.OnFragmentFilmListener
import com.spinoza.moviesforfintech.presentation.utils.SavedPosition
import com.spinoza.moviesforfintech.presentation.utils.getFilmFromBundle
import com.spinoza.moviesforfintech.presentation.utils.getSavedPositionFromBundle

class FilmDetailsFragment : Fragment() {

    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding: FragmentFilmDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentFilmDetailsBinding == null")

    private lateinit var film: Film
    private lateinit var savedPosition: SavedPosition

    private lateinit var fragmentFilmListener: OnFragmentFilmListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentFilmListener = context as OnFragmentFilmListener
    }

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
        fragmentFilmListener.saveFilm(film)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressedCallBack()
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
                imageViewArrow.setOnClickListener { openPreviousFragment() }
            }
        }
    }

    private fun setOnBackPressedCallBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                openPreviousFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun openPreviousFragment() {
        savedPosition.openDetails = false
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PopularFilmsFragment.newInstance(savedPosition)
            )
            .commit()
    }

    private fun parseArguments() {
        savedPosition = getSavedPositionFromBundle(requireArguments())
        film = getFilmFromBundle(requireArguments())
    }

    companion object {
        private const val KEY_FILM = "film"
        private const val KEY_SAVED_POSITION = "savedPosition"

        fun newInstance(film: Film, savedPosition: SavedPosition) = FilmDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_FILM, film)
                putParcelable(KEY_SAVED_POSITION, savedPosition)
            }
        }
    }
}