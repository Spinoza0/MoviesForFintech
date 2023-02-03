package com.spinoza.moviesforfintech.di

import com.spinoza.moviesforfintech.data.repository.FilmsRepositoryImpl
import com.spinoza.moviesforfintech.domain.repository.FilmsRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @ApplicationScope
    @Binds
    fun bindFilmsRepository(impl: FilmsRepositoryImpl): FilmsRepository
}