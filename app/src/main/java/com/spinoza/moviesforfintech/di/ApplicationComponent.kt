package com.spinoza.moviesforfintech.di

import android.content.Context
import com.spinoza.moviesforfintech.presentation.fragment.PopularFilmsFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DomainModule::class, DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(fragment: PopularFilmsFragment)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}