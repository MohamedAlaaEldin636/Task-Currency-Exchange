package my.ym.taskcurrencyexchange.models

open class BaseResponse(
	var success: Boolean? = null,
	val error: ErrorResponse? = null,
)

fun <T : BaseResponse> T.applySuccess(): T = apply { success = true }
