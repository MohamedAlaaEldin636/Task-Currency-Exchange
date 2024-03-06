package my.ym.taskcurrencyexchange.helperTypes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.extensions.isNetworkAvailable
import my.ym.taskcurrencyexchange.extensions.myApp
import my.ym.taskcurrencyexchange.models.BaseResponse

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {

	val showGlobalLoadingDialog = MutableLiveData<GlobalLoadingData?>(null)

	val showGlobalErrorHandlingDialog = MutableLiveData<GlobalErrorData?>(null)

	protected fun <T : BaseResponse> BaseAndroidViewModel.executeRetryAbleActionOrGoBack(
		action: suspend () -> Result<T>,
		scope: CoroutineScope = viewModelScope,
		onShowLoading: (() -> Unit)? = null,
		onHideLoading: (() -> Unit)? = null,
		onFailure: ((result: Result<T>, retryBlock: () -> Unit) -> Unit)? = null,
		onSuccess: (T?) -> Unit,
	): Job = executeRetryAbleActionOrFallback(
		action, scope, FallbackAction.GO_BACK, onShowLoading, onHideLoading, onFailure, onSuccess
	)

	protected fun <T : BaseResponse> BaseAndroidViewModel.executeRetryAbleActionOrCancel(
		action: suspend () -> Result<T>,
		scope: CoroutineScope = viewModelScope,
		onShowLoading: (() -> Unit)? = null,
		onHideLoading: (() -> Unit)? = null,
		onFailure: ((result: Result<T>, retryBlock: () -> Unit) -> Unit)? = null,
		onSuccess: (T?) -> Unit,
	): Job = executeRetryAbleActionOrFallback(
		action, scope, FallbackAction.CANCEL, onShowLoading, onHideLoading, onFailure, onSuccess
	)

	private fun <T : BaseResponse> BaseAndroidViewModel.executeRetryAbleActionOrFallback(
		action: suspend () -> Result<T>,
		scope: CoroutineScope = viewModelScope,
		fallbackAction: FallbackAction,
		onShowLoading: (() -> Unit)? = null,
		onHideLoading: (() -> Unit)? = null,
		onFailure: ((result: Result<T>, retryBlock: () -> Unit) -> Unit)? = null,
		onSuccess: (T?) -> Unit,
	): Job {
		val showLoading: (CoroutineScope) -> Unit = {
			if (onShowLoading == null) {
				showGlobalLoadingDialog.value = GlobalLoadingData(fallbackAction, it)
			}else {
				onShowLoading()
			}
		}

		val hideLoading: () -> Unit = {
			if (onHideLoading == null) {
				showGlobalLoadingDialog.value = null
			}else {
				onHideLoading()
			}
		}

		val onError: (Result<T>) -> Unit = {
			if (onFailure != null) {
				onFailure(it) {
					executeRetryAbleActionOrFallback(
						action, scope, fallbackAction, onShowLoading, onHideLoading, onFailure, onSuccess
					)
				}
			}else {
				showGlobalErrorHandlingDialog.value = GlobalErrorData(
					it.getErrorMsg(myApp),
					fallbackAction,
				) {
					executeRetryAbleActionOrFallback(
						action, scope, fallbackAction, onShowLoading, onHideLoading, null, onSuccess
					)
				}
			}
		}

		return executeRetryAbleActionGeneral(
			showLoading,
			hideLoading,
			scope,
			action,
			onError,
			onSuccess
		)
	}

	data class GlobalLoadingData(
		val fallbackAction: FallbackAction,
		val coroutineScope: CoroutineScope,
	)

	data class GlobalErrorData(
		val msg: String,
		val fallbackAction: FallbackAction,
		val retryAbleFun: () -> Unit
	)

	enum class FallbackAction {
		CANCEL, GO_BACK
	}

}

private fun <T : BaseResponse> executeRetryAbleActionGeneral(
	showLoading: (CoroutineScope) -> Unit,
	hideLoading: () -> Unit,
	scope: CoroutineScope,
	action: suspend () -> Result<T>,
	onError: (Result<T>) -> Unit,
	onSuccess: (T?) -> Unit,
): Job {
	return scope.launch {
		kotlin.runCatching {
			showLoading(this)
			val value = action()
			hideLoading()

			if (isActive.not()) {
				// Cancelled intentionally from [action] block isa.

				return@launch
			}

			if (value.isSuccess) {
				onSuccess(value.getOrNull())
			}else {
				onError(value)
			}
		}.getOrElse {
			// Cancelled intentionally from [action] block isa.
			if (it !is CancellationException) {
				onError(Result.failure(it))
			}
		}
	}
}

private fun Result<*>.getErrorMsg(context: Context): String {
	return context.run {
		when (val exception = exceptionOrNull()) {
			is ApiException -> {
				buildString {
					if (exception.code != null) {
						append("Code ${exception.code}, ")
					}

					when (exception.reason) {
						ApiException.Reason.CLIENT_ERROR -> {
							getString(R.string.something_happened_on_our_end_please_try_again_or_contact_us_if_persists)
						}
						ApiException.Reason.SERVER_ERROR -> {
							getString(R.string.there_is_a_problem_with_our_servers_please_try_again)
						}
						ApiException.Reason.API_ERROR -> {
							getString(R.string.unexpected_error_please_try_again_or_contact_us_if_persists)
						}
						ApiException.Reason.TIMEOUT_ERROR -> {
							getString(R.string.timeout_error_due_to_poor_internet_connection_check_connection_try_again)
						}
						ApiException.Reason.CONNECTION_ERROR -> {
							if (isNetworkAvailable()) {
								getString(R.string.unknown_connection_problem)
							}else {
								getString(R.string.no_internet_connection)
							}
						}
						ApiException.Reason.OTHER -> {
							getString(R.string.something_went_wrong_please_try_again)
						}
					}.also {
						append(it)
					}
				}
			}
			is Throwable -> {
				exception.message
			}
			else -> {
				// Should never happen isa, but an exhaustive when must be done.
				"Code UE-213"
			}
		}
	}.orEmpty()
}
