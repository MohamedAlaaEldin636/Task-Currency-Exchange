package my.ym.taskcurrencyexchange.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.ApiConversionsServices
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.ApiSymbolsServices
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServicesModule {

    @Provides
    @Singleton
    fun provideApiSettingsServices(retrofit: Retrofit): ApiSymbolsServices =
        retrofit.create(ApiSymbolsServices::class.java)

    @Provides
    @Singleton
    fun provideApiConversionsServices(retrofit: Retrofit): ApiConversionsServices =
        retrofit.create(ApiConversionsServices::class.java)

}
