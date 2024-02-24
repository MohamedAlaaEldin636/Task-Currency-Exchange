package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseConversion

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

}
