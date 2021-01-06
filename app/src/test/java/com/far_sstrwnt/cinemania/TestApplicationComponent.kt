package com.far_sstrwnt.cinemania

import com.far_sstrwnt.cinemania.di.ActivityBindingModule
import com.far_sstrwnt.cinemania.di.AppModule
import com.far_sstrwnt.cinemania.shared.di.DatabaseModule
import com.far_sstrwnt.cinemania.shared.di.NetworkModule
import com.far_sstrwnt.cinemania.shared.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        TestApplicationModule::class,
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelModule::class
    ])
interface TestApplicationComponent : AndroidInjector<TestMainApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: TestMainApplication): TestApplicationComponent
    }
}