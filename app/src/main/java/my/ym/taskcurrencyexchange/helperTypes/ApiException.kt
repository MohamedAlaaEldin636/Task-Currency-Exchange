package my.ym.taskcurrencyexchange.helperTypes

class ApiException(
	val reason: Reason,
	message: String?,
) : Exception("$reason -> $message") {

	constructor(reason: Reason, cause: Throwable?) : this(reason, cause?.message)

	enum class Reason {
		/** 4xx errors */
		CLIENT_ERROR,
		/** 5xx errors */
		SERVER_ERROR,
		/** returns success http 200 code yet api shows the exact error */
		API_ERROR,
		/** Connection error like have no internet connection or can't resolve the host or the server is down etc... */
		CONNECTION_ERROR,
		/** Any other reasons */
		OTHER
	}

}
