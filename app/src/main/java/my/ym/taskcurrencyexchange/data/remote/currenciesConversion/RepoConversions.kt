package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion
import my.ym.taskcurrencyexchange.models.ResponseRatesOfCurrencies
import java.time.LocalDate

interface RepoConversions {

	suspend fun convertCurrencyValue(
		fromCurrency: String,
		toCurrency: String,
		amountToConvert: Double,
	): Result<ResponseConversion>

	suspend fun getRatesOfCurrenciesForSpecificPeriod(
		startDate: LocalDate,
		endDate: LocalDate,
		baseCurrency: String,
		targetCurrencies: List<String>,
	): Result<ResponseRatesOfCurrencies>

}
