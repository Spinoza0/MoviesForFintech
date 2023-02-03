package com.spinoza.moviesforfintech.di

import android.content.Context
import com.spinoza.moviesforfintech.presentation.PopularFilmsActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DomainModule::class, DataModule::class])
interface ApplicationComponent {
    fun inject(activity: PopularFilmsActivity)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}