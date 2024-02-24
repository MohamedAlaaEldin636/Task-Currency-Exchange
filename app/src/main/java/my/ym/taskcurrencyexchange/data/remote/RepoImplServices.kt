package my.ym.taskcurrencyexchange.data.remote

class RepoImplServices(
	private val remoteDataSource: RemoteDataSourceSymbols
) : RepoServices {

	override suspend fun getAllCurrenciesSymbols() =
		remoteDataSource.getAllCurrenciesSymbols()

}
