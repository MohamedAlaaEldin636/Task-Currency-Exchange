package my.ym.taskcurrencyexchange.data.remote

import my.ym.taskcurrencyexchange.models.ResponseSymbols

interface RepoServices {

	suspend fun getAllCurrenciesSymbols(): Result<ResponseSymbols>

}
