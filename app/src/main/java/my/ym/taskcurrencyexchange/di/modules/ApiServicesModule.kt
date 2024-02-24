package my.ym.taskcurrencyexchange.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import my.ym.taskcurrencyexchange.data.remote.ApiSymbolsServices
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ApiServicesModule {

    @Provides
    @Singleton
    fun provideApiSettingsServices(retrofit: Retrofit): ApiSymbolsServices =
        retrofit.create(ApiSymbolsServices::class.java)

}
