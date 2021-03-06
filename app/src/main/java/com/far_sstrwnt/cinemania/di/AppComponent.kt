package com.far_sstrwnt.cinemania.di

import com.far_sstrwnt.cinemania.MainApplication
import com.far_sstrwnt.cinemania.shared.di.DatabaseModule
import com.far_sstrwnt.cinemania.shared.di.NetworkModule
import com.far_sstrwnt.cinemania.shared.di.ApplicationModule
import com.far_sstrwnt.cinemania.shared.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ApplicationModule::class,
        ViewModelModule::class
    ])
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: MainApplication): AppComponent
    }
}