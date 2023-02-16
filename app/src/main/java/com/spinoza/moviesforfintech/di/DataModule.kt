package com.spinoza.moviesforfintech.di

import android.content.Context
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.database.FilmsDatabase
import com.spinoza.moviesforfintech.data.network.ApiFactory
import com.spinoza.moviesforfintech.data.network.ApiService
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @ApplicationScope
        @Provides
        fun provideFilmsDao(context: Context): FilmsDao =
            FilmsDatabase.getInstance(context).filmsDao()

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}