package com.spinoza.moviesforfintech.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spinoza.moviesforfintech.R
import com.spinoza.moviesforfintech.databinding.FragmentFilmsListBinding
import com.spinoza.moviesforfintech.di.DaggerApplicationComponent
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmsState
import com.spinoza.moviesforfintech.domain.repository.ScreenType
import com.spinoza.moviesforfintech.presentation.activity.OnFragmentSavedPositionListener
import com.spinoza.moviesforfintech.presentation.adapter.FilmsListAdapter
import com.spinoza.moviesforfintech.presentation.utils.KEY_SAVED_POSITION
import com.spinoza.moviesforfintech.presentation.utils.SavedPosition
import com.spinoza.moviesforfintech.presentation.utils.getSavedPositionFromBundle
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

    private var currentScreenType = ScreenType.POPULAR

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filmsAdapter: FilmsListAdapter

    private val popularScreenSavedPosition = SavedPosition(
        screenType = ScreenType.POPULAR,
        position = 0,
        needRestore = false,
        openDetails = false
    )
    private val favouriteScreenSavedPosition = SavedPosition(
        screenType = ScreenType.FAVOURITE,
        position = 0,
        needRestore = false,
        openDetails = false
    )

    private val itemTouchHelper by lazy {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.changeFavouriteStatus(filmsAdapter.currentList[viewHolder.adapterPosition])
            }
        })
    }

    private lateinit var fragmentSendDataListener: OnFragmentSavedPositionListener
    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
        fragmentSendDataListener = context as OnFragmentSavedPositionListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilmsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressedCallBack()
        val savedPosition = parseArguments()
        currentScreenType = savedPosition.screenType
        if (savedPosition.screenType == ScreenType.POPULAR) {
            restoreScreenSavedPosition(savedPosition, popularScreenSavedPosition)
        } else {
            restoreScreenSavedPosition(savedPosition, favouriteScreenSavedPosition)
        }
        setupScreen()
    }

    private fun setupScreen() {
        setupRecyclerView()
        setupObservers()
        switchSourceTo(currentScreenType)
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
            viewModel.state.observe(viewLifecycleOwner) {
                if (it !is FilmsState.Loading) {
                    progressBar.visibility = GONE
                }
                when (it) {
                    is FilmsState.Loading -> progressBar.visibility = VISIBLE
                    is FilmsState.Error -> showError(it.value)
                    is FilmsState.AllFilms -> {
                        filmsAdapter.submitList(it.value) {
                            when (currentScreenType) {
                                ScreenType.POPULAR -> {
                                    restoreRecyclerViewPosition(popularScreenSavedPosition)
                                }
                                else -> {
                                    restoreRecyclerViewPosition(favouriteScreenSavedPosition)
                                }
                            }
                        }
                    }
                    is FilmsState.OneFilm -> showFileInfo(it.value)
                }
            }

            viewModel.screenType.observe(viewLifecycleOwner) {
                currentScreenType = it
                when (it) {
                    ScreenType.POPULAR -> {
                        setButtonOn(textViewButtonFavourite, ScreenType.FAVOURITE)
                        setButtonOff(textViewButtonPopular)
                        textViewPageTitlePopular.visibility = VISIBLE
                        textViewPageTitleFavourite.visibility = INVISIBLE
                        removeSwipeFromRecyclerView()
                    }
                    else -> {
                        setButtonOn(textViewButtonPopular, ScreenType.POPULAR)
                        setButtonOff(textViewButtonFavourite)
                        textViewPageTitlePopular.visibility = INVISIBLE
                        textViewPageTitleFavourite.visibility = VISIBLE
                        addSwipeToRecyclerView()
                    }
                }
            }
        }
    }

    private fun addSwipeToRecyclerView() {
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewList)
    }

    private fun removeSwipeFromRecyclerView() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    private fun restoreRecyclerViewPosition(savedPosition: SavedPosition) {
        if (savedPosition.needRestore) {
            binding.recyclerViewList.scrollToPosition(savedPosition.position)
            savedPosition.needRestore = false
        }
    }

    private fun saveRecyclerViewPosition(savedPosition: SavedPosition) = savedPosition.apply {
        val position = getFirstVisiblePosition()
        if (position != -1) {
            this.needRestore = true
            this.position = position
        } else {
            this.needRestore = false
            this.position = DEFAULT_VISIBLE_POSITION
        }
    }

    private fun restoreScreenSavedPosition(source: SavedPosition, target: SavedPosition) {
        target.needRestore = source.needRestore
        target.position = source.position
    }

    private fun switchSourceTo(target: ScreenType) {
        when (target) {
            ScreenType.POPULAR -> {
                saveRecyclerViewPosition(favouriteScreenSavedPosition)
            }
            else -> {
                saveRecyclerViewPosition(popularScreenSavedPosition)
            }
        }
        viewModel.switchSourceTo(target)
    }

    private fun saveScreenPosition(openDetails: Boolean): SavedPosition {
        val savedPosition = when (currentScreenType) {
            ScreenType.POPULAR -> {
                saveRecyclerViewPosition(popularScreenSavedPosition)
            }
            else -> {
                saveRecyclerViewPosition(favouriteScreenSavedPosition)
            }
        }.copy(screenType = currentScreenType, openDetails = openDetails)
        fragmentSendDataListener.savePosition(savedPosition)
        return savedPosition
    }

    private fun setButtonOff(textView: TextView) {
        textView.setTextColor(colorButtonOff)
        textView.background.setTint(colorBackgroundButtonOff)
        textView.setOnClickListener(null)
    }

    private fun setButtonOn(textView: TextView, targetMode: ScreenType) {
        textView.setTextColor(colorButtonOn)
        textView.background.setTint(colorBackgroundButtonOn)
        textView.setOnClickListener { switchSourceTo(targetMode) }
    }

    private fun getColor(colorRes: Int) = ContextCompat.getColor(requireContext(), colorRes)

    private fun showFileInfo(film: Film) {
        if (isOnePanelMode()) {
            val savedPosition = saveScreenPosition(true)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    FilmDetailsFragment.newInstance(film, savedPosition)
                )
                .commit()

        } else {
            binding.textViewName?.text = film.nameRu
            binding.textViewDescription?.text = film.description
        }
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "$loadingError: $error", Toast.LENGTH_LONG).show()
    }

    private fun getFirstVisiblePosition(): Int =
        if (binding.recyclerViewList.layoutManager is LinearLayoutManager) {
            (binding.recyclerViewList.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
        } else {
            DEFAULT_VISIBLE_POSITION
        }

    private fun isOnePanelMode(): Boolean = binding.textViewDescription == null
    private fun parseArguments() = getSavedPositionFromBundle(requireArguments())

    private fun setOnBackPressedCallBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentScreenType == ScreenType.FAVOURITE) {
                    switchSourceTo(ScreenType.POPULAR)
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    companion object {
        private const val DEFAULT_VISIBLE_POSITION = 0
        fun newInstance(savedPosition: SavedPosition) = PopularFilmsFragment().apply {
            arguments = Bundle().apply { putParcelable(KEY_SAVED_POSITION, savedPosition) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveScreenPosition(false)
    }
}