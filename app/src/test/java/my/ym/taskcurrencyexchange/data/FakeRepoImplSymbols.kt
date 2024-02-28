package my.ym.taskcurrencyexchange.data

import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoSymbols
import my.ym.taskcurrencyexchange.models.ResponseSymbols

object FakeRepoImplSymbols {

	const val CURRENCY_EGP = "EGP"
	const val CURRENCY_USD = "USD"

	/** This isn't true, just a fak data */
	const val RATIO_CONVERSION_EGP_TO_USD = 2.0
	private const val RATIO_CONVERSION_USD_TO_EGP = 1.div(RATIO_CONVERSION_EGP_TO_USD)

	private const val RATIO_CONVERSION_EGP_TO_OTHER_CURRENCIES_EXCEPT_USD = 3.0
	private const val RATIO_CONVERSION_USD_TO_OTHER_CURRENCIES_EXCEPT_EGP =
		RATIO_CONVERSION_EGP_TO_OTHER_CURRENCIES_EXCEPT_USD.div(
			RATIO_CONVERSION_EGP_TO_USD
		)

	fun getRateOfConversion(baseCurrency: String, targetCurrency: String): Double {
		return when {
			// EGP cases
			baseCurrency == CURRENCY_EGP && targetCurrency == CURRENCY_EGP -> 1.0
			baseCurrency == CURRENCY_EGP && targetCurrency == CURRENCY_USD -> RATIO_CONVERSION_EGP_TO_USD
			baseCurrency == CURRENCY_EGP -> RATIO_CONVERSION_EGP_TO_OTHER_CURRENCIES_EXCEPT_USD
			// USD cases
			baseCurrency == CURRENCY_USD && targetCurrency == CURRENCY_USD -> 1.0
			baseCurrency == CURRENCY_USD && targetCurrency == CURRENCY_EGP -> RATIO_CONVERSION_USD_TO_EGP
			baseCurrency == CURRENCY_USD -> RATIO_CONVERSION_USD_TO_OTHER_CURRENCIES_EXCEPT_EGP
			// Other cases
			targetCurrency == CURRENCY_EGP -> 1.div(RATIO_CONVERSION_EGP_TO_OTHER_CURRENCIES_EXCEPT_USD)
			targetCurrency == CURRENCY_USD -> 1.div(RATIO_CONVERSION_USD_TO_OTHER_CURRENCIES_EXCEPT_EGP)
			else /* other to other */ -> 1.0
		}
	}

	object Success : RepoSymbols {

		override suspend fun getAllCurrenciesSymbols(): Result<ResponseSymbols> {
			return Result.success(
				ResponseSymbols(
					mapOf(
						CURRENCY_EGP to "Egyptian Pound",
						CURRENCY_USD to "United States Dollar",
					)
				)
			)
		}

	}

}
