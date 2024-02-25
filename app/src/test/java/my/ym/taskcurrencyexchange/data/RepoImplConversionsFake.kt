package my.ym.taskcurrencyexchange.data

import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoConversions
import my.ym.taskcurrencyexchange.models.ResponseConversion

object RepoImplConversionsFake {

	object Success : RepoConversions {

		override suspend fun convertCurrencyValue(
			fromCurrency: String,
			toCurrency: String,
			amountToConvert: Double
		): Result<ResponseConversion> {
			return when {
				fromCurrency == RepoImplSymbolsFake.CURRENCY_EGP
					&& toCurrency == RepoImplSymbolsFake.CURRENCY_EGP -> {
						Result.success(
							ResponseConversion(
								amountToConvert,
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == RepoImplSymbolsFake.CURRENCY_EGP
					&& toCurrency == RepoImplSymbolsFake.CURRENCY_USD -> {
						Result.success(
							ResponseConversion(
								amountToConvert.times(RepoImplSymbolsFake.RATIO_CONVERSION_EGP_TO_USD),
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == RepoImplSymbolsFake.CURRENCY_USD
					&& toCurrency == RepoImplSymbolsFake.CURRENCY_USD -> {
						Result.success(
							ResponseConversion(
								amountToConvert,
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == RepoImplSymbolsFake.CURRENCY_USD
					&& toCurrency == RepoImplSymbolsFake.CURRENCY_EGP -> {
						Result.success(
							ResponseConversion(
								amountToConvert.div(RepoImplSymbolsFake.RATIO_CONVERSION_EGP_TO_USD),
								null,
								null,
								null,
							)
						)
					}
				else -> {
					throw RuntimeException("Not supported currencies in Fake repos")
				}
			}
		}
	}

}
