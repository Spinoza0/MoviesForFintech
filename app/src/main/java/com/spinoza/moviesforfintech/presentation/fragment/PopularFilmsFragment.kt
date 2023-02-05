package com.spinoza.moviesforfintech.presentation.fragment

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.FragmentFilmsListBinding
import com.spinoza.moviesforfintech.di.DaggerApplicationComponent
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.domain.repository.SourceType
import com.spinoza.moviesforfintech.presentation.activity.FilmDetailsActivity
import com.spinoza.moviesforfintech.presentation.activity.OnFragmentSendDataListener
import com.spinoza.moviesforfintech.presentation.adapter.FilmsListAdapter
import com.spinoza.moviesforfintech.presentation.utils.SOURCE_TYPE
import com.spinoza.moviesforfintech.presentation.utils.SavedPosition
import com.spinoza.moviesforfintech.presentation.utils.getSourceTypeFromBundle
import com.spinoza.moviesforfintech.presentation.viewmodel.PopularFilmsViewModel
import com.spinoza.moviesforfintech.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class PopularFilmsFragment : Fragment() {
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
    private val loadingError by lazy { getString(R.string.loading_error) }

    private lateinit var currentSourceType: SourceType

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filmsAdapter: FilmsListAdapter

    private val popularScreenSavedPosition = SavedPosition(
        SourceType.POPULAR,
        0,
        false
    )
    private val favouriteScreenSavedPosition = SavedPosition(
        SourceType.FAVOURITE,
        0,
        false
    )

    lateinit var fragmentSendDataListener: OnFragmentSendDataListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendDataListener = context as OnFragmentSendDataListener
    }

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
        switchSourceTo(parseArguments())
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
                    filmsAdapter.submitList(it.films) {
                        when (currentSourceType) {
                            SourceType.POPULAR -> {
                                restoreRecyclerViewPosition(popularScreenSavedPosition)
                            }
                            else -> {
                                restoreRecyclerViewPosition(favouriteScreenSavedPosition)
                            }
                        }
                    }
                }
            }
            viewModel.oneFilmResponse.observe(viewLifecycleOwner) {
                if (it.error.isNotEmpty()) {
                    showError(it)
                } else {
                    showFileInfo(it.films[0])
                }
            }
            viewModel.sourceType.observe(viewLifecycleOwner) {
                currentSourceType = it
                when (it) {
                    SourceType.POPULAR -> {
                        setButtonOn(textViewButtonFavourite, SourceType.FAVOURITE)
                        setButtonOff(textViewButtonPopular)
                        textViewPageTitlePopular.visibility = VISIBLE
                        textViewPageTitleFavourite.visibility = INVISIBLE
                    }
                    else -> {
                        setButtonOn(textViewButtonPopular, SourceType.POPULAR)
                        setButtonOff(textViewButtonFavourite)
                        textViewPageTitlePopular.visibility = INVISIBLE
                        textViewPageTitleFavourite.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun restoreRecyclerViewPosition(savedPosition: SavedPosition) {
        if (savedPosition.needRestore) {
            binding.recyclerViewList.scrollToPosition(savedPosition.position)
            savedPosition.needRestore = false
        }
    }

    private fun saveRecyclerViewPosition(savedPosition: SavedPosition) {
        savedPosition.needRestore = true
        savedPosition.position = getFirstVisiblePosition()
    }

    private fun switchSourceTo(target: SourceType) {
        when (target) {
            SourceType.POPULAR -> {
                saveRecyclerViewPosition(favouriteScreenSavedPosition)
            }
            else -> {
                saveRecyclerViewPosition(popularScreenSavedPosition)
            }
        }
        fragmentSendDataListener(target)
        viewModel.switchSourceTo(target)
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
        Toast.makeText(requireContext(), "$loadingError: ${it.error}", Toast.LENGTH_LONG).show()
    }

    private fun getFirstVisiblePosition(): Int {
        return if (binding.recyclerViewList.layoutManager is LinearLayoutManager) {
            (binding.recyclerViewList.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
        } else {
            DEFAULT_VISIBLE_POSITION
        }
    }

    private fun isOnePanelMode(): Boolean = binding.textViewDescription == null
    private fun parseArguments() = getSourceTypeFromBundle(requireArguments())

    companion object {
        private const val DEFAULT_VISIBLE_POSITION = 0
        fun newInstance(sourceType: SourceType) = PopularFilmsFragment().apply {
            arguments = Bundle().apply { putParcelable(SOURCE_TYPE, sourceType) }
        }
    }
}