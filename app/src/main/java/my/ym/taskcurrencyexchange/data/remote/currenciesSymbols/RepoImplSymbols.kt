package my.ym.taskcurrencyexchange.data.remote.currenciesSymbols

class RepoImplSymbols(
	private val remoteDataSource: RemoteDataSourceSymbols
) : RepoSymbols {

	override suspend fun getAllCurrenciesSymbols() =
		remoteDataSource.getAllCurrenciesSymbols()

}
