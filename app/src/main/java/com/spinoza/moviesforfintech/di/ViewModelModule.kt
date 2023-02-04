package com.spinoza.moviesforfintech.di

import androidx.lifecycle.ViewModel
import com.spinoza.moviesforfintech.presentation.viewmodel.PopularFilmsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface ViewModelModule {
    @IntoMap
    @StringKey("PopularFilmsViewModel")
    @Binds
    fun bindPopularFilmsViewModel(impl: PopularFilmsViewModel): ViewModel
}