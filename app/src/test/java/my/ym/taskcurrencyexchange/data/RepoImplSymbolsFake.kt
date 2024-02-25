package my.ym.taskcurrencyexchange.data

import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoSymbols
import my.ym.taskcurrencyexchange.models.ResponseSymbols

object RepoImplSymbolsFake {

	const val CURRENCY_EGP = "EGP"
	const val CURRENCY_USD = "USD"

	/** This isn't true, just a fak data */
	const val RATIO_CONVERSION_EGP_TO_USD = 2

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
