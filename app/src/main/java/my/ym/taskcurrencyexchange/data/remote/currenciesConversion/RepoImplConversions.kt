package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion
import my.ym.taskcurrencyexchange.models.ResponseRatesOfCurrencies
import java.time.LocalDate

class RepoImplConversions(
	private val remoteDataSource: RemoteDataSourceConversions
) : RepoConversions {

	override suspend fun convertCurrencyValue(
		fromCurrency: String,
		toCurrency: String,
		amountToConvert: Double
	): Result<ResponseConversion> {
		return remoteDataSource.convertCurrencyValue(fromCurrency, toCurrency, amountToConvert)
	}

	override suspend fun getRatesOfCurrenciesForSpecificPeriod(
		startDate: LocalDate,
		endDate: LocalDate,
		baseCurrency: String,
		targetCurrencies: List<String>
	): Result<ResponseRatesOfCurrencies> {
		return remoteDataSource.getRatesOfCurrenciesForSpecificPeriod(startDate, endDate, baseCurrency, targetCurrencies)
	}

}
