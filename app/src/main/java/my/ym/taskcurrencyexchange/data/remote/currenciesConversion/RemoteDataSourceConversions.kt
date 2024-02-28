package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.extensions.convertToFormatYYYYMMDD
import my.ym.taskcurrencyexchange.helperTypes.BaseRemoteDataSource
import java.time.LocalDate
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

	suspend fun getRatesOfCurrenciesForSpecificPeriod(
		startDate: LocalDate,
		endDate: LocalDate,
		baseCurrency: String,
		targetCurrencies: List<String>,
	) = safeApiCall {
		// Params not like ones in the Api interface as it's more code friendly, also to avoid
		// error-prone cases isa.
		apiService.getRatesOfCurrenciesForSpecificPeriod(
			startDate.convertToFormatYYYYMMDD(),
			endDate.convertToFormatYYYYMMDD(),
			baseCurrency,
			targetCurrencies.joinToString(",")
		)
	}

}
