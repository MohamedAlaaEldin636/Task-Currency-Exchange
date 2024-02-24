package my.ym.taskcurrencyexchange.models

/**
 * @param symbols Contains key value pairs of currency symbol and its name, Ex.
 * "AED": "United Arab Emirates Dirham"
 */
data class ResponseSymbols(
	val symbols: Map<String, String>?,
) : BaseResponse()
