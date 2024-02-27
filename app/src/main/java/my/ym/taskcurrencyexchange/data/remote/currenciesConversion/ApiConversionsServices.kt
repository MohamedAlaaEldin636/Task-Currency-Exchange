package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion
import my.ym.taskcurrencyexchange.models.ResponseRatesOfCurrencies
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiConversionsServices {

	@GET("convert")
	suspend fun convertCurrencyValue(
		@Query("from") fromCurrency: String,
		@Query("to") toCurrency: String,
		@Query("amount") amountToConvert: Double,
	): ResponseConversion

	/**
	 * @param startDate YYYY-MM-DD use 02 in case of was trying to use 2 isa.
	 *
	 * @param targetCurrencies CSV, Ex. USD,CAD,JPY
	 */
	@GET("timeseries")
	suspend fun getRatesOfCurrenciesForSpecificPeriod(
		@Query("start_date") startDate: String,
		@Query("end_date") endDate: String,
		@Query("base") baseCurrency: String,
		@Query("symbols") targetCurrencies: String,
	): ResponseRatesOfCurrencies

}
