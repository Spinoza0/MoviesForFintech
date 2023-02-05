package com.spinoza.moviesforfintech.di

import androidx.lifecycle.ViewModel
import com.spinoza.moviesforfintech.presentation.viewmodel.PopularFilmsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(PopularFilmsViewModel::class)
    @Binds
    fun bindPopularFilmsViewModel(impl: PopularFilmsViewModel): ViewModel
}