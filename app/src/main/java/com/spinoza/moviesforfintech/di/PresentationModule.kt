package com.spinoza.moviesforfintech.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val context: Context) {
    @Provides
    fun provideContext() = context
}