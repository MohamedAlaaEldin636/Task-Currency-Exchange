package my.ym.taskcurrencyexchange.data.remote.currenciesSymbols

import my.ym.taskcurrencyexchange.helperTypes.BaseRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceSymbols @Inject constructor(
	private val apiService: ApiSymbolsServices,
) : BaseRemoteDataSource() {

	suspend fun getAllCurrenciesSymbols() = safeApiCall {
		apiService.getAllCurrenciesSymbols()
	}

}
