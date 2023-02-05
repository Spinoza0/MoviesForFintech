package com.spinoza.moviesforfintech.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.FragmentFilmsListBinding
import com.spinoza.moviesforfintech.di.DaggerApplicationComponent
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.domain.repository.SourceType
import com.spinoza.moviesforfintech.presentation.activity.FilmDetailsActivity
import com.spinoza.moviesforfintech.presentation.adapter.FilmsListAdapter
import com.spinoza.moviesforfintech.presentation.viewmodel.PopularFilmsViewModel
import com.spinoza.moviesforfintech.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class PopularFilmsFragment : Fragment() {
    private var sourceType = SourceType.POPULAR
    private var _binding: FragmentFilmsListBinding? = null
    private val binding: FragmentFilmsListBinding
        get() = _binding ?: throw RuntimeException("FragmentFilmsListBinding == null")

    private val component by lazy {
        DaggerApplicationComponent.factory().create(requireContext())
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularFilmsViewModel::class.java]
    }
    private val colorButtonOn by lazy { getColor(R.color.text_button_on) }
    private val colorButtonOff by lazy { getColor(R.color.text_button_off) }
    private val colorBackgroundButtonOn by lazy { getColor(R.color.background_button_on) }
    private val colorBackgroundButtonOff by lazy { getColor(R.color.background_button_off) }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filmsAdapter: FilmsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        component.inject(this)
        _binding = FragmentFilmsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreen()
    }

    private fun setupScreen() {
        setupRecyclerView()
        setupObservers()
        switchSourceTo(sourceType)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewList.adapter = filmsAdapter
        binding.recyclerViewList.itemAnimator = null
        filmsAdapter.onFilmItemClickListener = { viewModel.loadFullFilmData(it.filmId) }
        filmsAdapter.onFilmItemLongClickListener = { viewModel.changeFavouriteStatus(it) }
        filmsAdapter.onReachEndListener = { viewModel.loadAllFilms() }
    }

    private fun setupObservers() {
        with(binding) {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                progressBar.visibility = if (it) VISIBLE else GONE
            }
            viewModel.allFilmsResponse.observe(viewLifecycleOwner) {
                if (it.error.isNotEmpty()) {
                    showError(it)
                } else {
                    filmsAdapter.submitList(it.films)
                }
            }
            viewModel.oneFilmResponse.observe(viewLifecycleOwner) {
                if (it.error.isNotEmpty()) {
                    showError(it)
                } else {
                    showFileInfo(it.films[0])
                }
            }
        }
    }

    private fun switchSourceTo(target: SourceType) {
        sourceType = target
        setupListeners()
        viewModel.switchSourceTo(target)
    }

    private fun setupListeners() {
        with(binding) {
            if (sourceType == SourceType.POPULAR) {
                setButtonOn(textViewButtonFavourite, SourceType.FAVOURITE)
                setButtonOff(textViewButtonPopular)
                textViewPageTitlePopular.visibility = VISIBLE
                textViewPageTitleFavourite.visibility = INVISIBLE
            } else if (sourceType == SourceType.FAVOURITE) {
                setButtonOn(textViewButtonPopular, SourceType.POPULAR)
                setButtonOff(textViewButtonFavourite)
                textViewPageTitlePopular.visibility = INVISIBLE
                textViewPageTitleFavourite.visibility = VISIBLE
            }
        }
    }

    private fun setButtonOff(textView: TextView) {
        textView.setTextColor(colorButtonOff)
        textView.background.setTint(colorBackgroundButtonOff)
        textView.setOnClickListener(null)
    }

    private fun setButtonOn(textView: TextView, targetMode: SourceType) {
        textView.setTextColor(colorButtonOn)
        textView.background.setTint(colorBackgroundButtonOn)
        textView.setOnClickListener { switchSourceTo(targetMode) }
    }

    private fun getColor(colorRes: Int) = ContextCompat.getColor(requireContext(), colorRes)

    private fun showFileInfo(film: Film) {
        if (isOnePanelMode()) {
            startActivity(FilmDetailsActivity.newIntent(requireContext(), film))
        } else {
            binding.textViewName?.text = film.nameRu
            binding.textViewDescription?.text = film.description
        }
    }

    private fun showError(it: FilmResponse) {
        // TODO: вывод сообщения как в ТЗ
        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
    }

    private fun isOnePanelMode(): Boolean = binding.textViewDescription == null

    companion object {
        fun newInstance() = PopularFilmsFragment()
    }
}