package com.example.photovideoclockframe.presentation.main.di

import com.example.photovideoclockframe.presentation.main.MainActivity
import com.example.photovideoclockframe.presentation.main.MainContract
import com.example.photovideoclockframe.presentation.main.MainPresenter
import com.example.photovideoclockframe.utility.MediaPathLoader
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@ActivityScope
@Component(modules = [MainModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Module
class MainModule(private val activity: MainActivity) {

    @ActivityScope
    @Provides
    fun providesView(): MainContract.View = activity

    @ActivityScope
    @Provides
    fun providesActivity(): MainActivity = activity

    @ActivityScope
    @Provides
    fun providesPermissionsManager(activity: MainActivity) = PermissionsManager(activity)

    @ActivityScope
    @Provides
    fun providesMediaPathLoader() = MediaPathLoader()

    @ActivityScope
    @Provides
    fun providesPresenter(
        view: MainContract.View,
        permissionsManager: PermissionsManager,
        mediaPathLoader: MediaPathLoader
    ): MainContract.Presenter =
        MainPresenter(view, permissionsManager, mediaPathLoader)
}