package my.ym.taskcurrencyexchange.data.remote.currenciesSymbols

import my.ym.taskcurrencyexchange.models.ResponseSymbols
import retrofit2.http.GET

interface ApiSymbolsServices {

	@GET("symbols")
	suspend fun getAllCurrenciesSymbols(): ResponseSymbols

}
