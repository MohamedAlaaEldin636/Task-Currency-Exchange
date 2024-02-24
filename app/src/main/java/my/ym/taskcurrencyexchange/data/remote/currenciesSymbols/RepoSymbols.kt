package my.ym.taskcurrencyexchange.data.remote.currenciesSymbols

import my.ym.taskcurrencyexchange.models.ResponseSymbols

interface RepoSymbols {

	suspend fun getAllCurrenciesSymbols(): Result<ResponseSymbols>

}
