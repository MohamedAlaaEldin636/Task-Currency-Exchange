package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion

interface RepoConversions {

	suspend fun convertCurrencyValue(
		fromCurrency: String,
		toCurrency: String,
		amountToConvert: Double,
	): Result<ResponseConversion>

}
