package my.ym.taskcurrencyexchange.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import my.ym.taskcurrencyexchange.data.remote.RemoteDataSourceSymbols
import my.ym.taskcurrencyexchange.data.remote.RepoServices
import my.ym.taskcurrencyexchange.data.remote.RepoImplServices

/**
 * - Scoped to view model since in MVVM arch. pattern only view model should be able
 * to interact with the data sources.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ReposModule {

	@Provides
	fun provideRepoServices(
		remoteDataSourceSymbols: RemoteDataSourceSymbols
	): RepoServices = RepoImplServices(remoteDataSourceSymbols)

}
