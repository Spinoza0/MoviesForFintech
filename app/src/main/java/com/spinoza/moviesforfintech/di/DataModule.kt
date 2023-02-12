package com.spinoza.moviesforfintech.di

import android.content.Context
import com.spinoza.moviesforfintech.data.database.FilmsDao
import com.spinoza.moviesforfintech.data.database.FilmsDatabase
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun provideFilmsDao(context: Context): FilmsDao = FilmsDatabase.getInstance(context).filmsDao()
}