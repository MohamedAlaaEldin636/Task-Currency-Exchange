package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiConversionsServices {

	@GET("convert")
	suspend fun convertCurrencyValue(
		@Query("from") fromCurrency: String,
		@Query("to") toCurrency: String,
		@Query("amount") amountToConvert: Double,
	): ResponseConversion

}
