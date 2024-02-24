package my.ym.taskcurrencyexchange.models

data class ResponseConversion(
	val result: Double?,
	val date: String?,
	val info: ApiInfo?,
	val query: ApiQuery?,
) : BaseResponse()
