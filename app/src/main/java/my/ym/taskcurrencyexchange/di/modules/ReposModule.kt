package my.ym.taskcurrencyexchange.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RemoteDataSourceConversions
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoConversions
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoImplConversions
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RemoteDataSourceSymbols
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoSymbols
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoImplSymbols

/**
 * - Scoped to view model since in MVVM arch. pattern only view model should be able
 * to interact with the data sources.
 */
@Module
@InstallIn(SingletonComponent::class)
object ReposModule {

	@Provides
	fun provideRepoServices(
		remoteDataSourceSymbols: RemoteDataSourceSymbols
	): RepoSymbols = RepoImplSymbols(remoteDataSourceSymbols)

	@Provides
	fun provideRepoConversions(
		remoteDataSourceConversions: RemoteDataSourceConversions
	): RepoConversions = RepoImplConversions(remoteDataSourceConversions)

}
