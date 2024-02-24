package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.helperTypes.BaseRemoteDataSource
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceConversions @Inject constructor(
	private val apiService: ApiConversionsServices,
) : BaseRemoteDataSource() {

	suspend fun convertCurrencyValue(
		fromCurrency: String,
		toCurrency: String,
		amountToConvert: Double,
	) = safeApiCall {
		apiService.convertCurrencyValue(fromCurrency, toCurrency, amountToConvert)
	}

}
