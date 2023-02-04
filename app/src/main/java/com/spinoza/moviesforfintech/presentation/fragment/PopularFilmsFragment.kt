package com.spinoza.moviesforfintech.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spinoza.moviesforfintech.databinding.FragmentFilmsListBinding
import com.spinoza.moviesforfintech.di.DaggerApplicationComponent
import com.spinoza.moviesforfintech.domain.model.Film
import com.spinoza.moviesforfintech.domain.model.FilmResponse
import com.spinoza.moviesforfintech.presentation.adapter.FilmsListAdapter
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filmsAdapter: FilmsListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PopularFilmsViewModel::class.java]
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
    }

    private fun setupRecyclerView() {
        binding.recyclerViewList.adapter = filmsAdapter
        binding.recyclerViewList.itemAnimator = null
        filmsAdapter.onFilmItemClickListener = { viewModel.loadFullFilmData(it.filmId) }
        filmsAdapter.onFilmItemLongClickListener = { viewModel.changeFavouriteStatus(it) }
        filmsAdapter.onReachEndListener = { viewModel.loadFilms() }
    }

    private fun setupObservers() {
        with(binding) {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            viewModel.filmsResponse.observe(viewLifecycleOwner) {
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

    private fun showFileInfo(film: Film) {
        if (!isOnePanelMode()) {
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