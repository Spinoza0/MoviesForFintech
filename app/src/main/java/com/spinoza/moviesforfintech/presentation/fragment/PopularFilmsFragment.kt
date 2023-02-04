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
        filmsAdapter.onFilmItemClickListener = {

        }
        filmsAdapter.onFilmItemLongClickListener = {

        }
        filmsAdapter.onReachEndListener = { viewModel.loadMovies() }
    }

    private fun setupObservers() {
        with(binding) {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            viewModel.films.observe(viewLifecycleOwner) { filmsAdapter.submitList(it) }
            viewModel.isError.observe(viewLifecycleOwner) {
                // TODO: вывод сообщения как в ТЗ
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isOnePanelMode(): Boolean = binding.textViewDescription == null

    companion object {
        fun newInstance() = PopularFilmsFragment()
    }
}