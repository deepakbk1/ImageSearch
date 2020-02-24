package com.deepak.imagesearch.injection

import com.deepak.imagesearch.App
import com.deepak.imagesearch.ui.MainActivity
import com.deepak.imagesearch.injection.scopes.PerApp
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@PerApp
@dagger.Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        BuildersModule::class]
)
interface AppComponent : AndroidInjector<MainActivity> {

    fun inject(application: App)
}
