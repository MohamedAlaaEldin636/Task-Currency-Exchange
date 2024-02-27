package my.ym.taskcurrencyexchange.extensions

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.helperTypes.ApiException
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment
import my.ym.taskcurrencyexchange.models.BaseResponse
import timber.log.Timber

private enum class NegativeAction {
	CANCEL, GO_BACK
}

/**
 * - Handles for you execution of the api call and showing loading until it returns a result,
 * and in case of failure it shows a dialog to try again or go back if can't cancel, Ex. consider
 * a screen that depends on specific data to show results then data must be given or return to
 * previous screen like a details screen of a product, However there can be another case where just
 * cancel without going back is enough like on button click for ex. Register so on error just cancel
 * if don't want to try again, like [executeRetryAbleActionOrCancel] isa.
 */
fun <T : BaseResponse> BaseFragment<*>.executeRetryAbleActionOrGoBack(
	action: suspend () -> Result<T>,
	scope: CoroutineScope = lifecycleScope,
	onShowLoading: (() -> Unit)? = null,
	onHideLoading: (() -> Unit)? = null,
	onError: (Result<T>) -> Unit = {},
	onSuccess: (T?) -> Unit,
): Job = executeRetryAbleActionOrEitherCancelOrGoBack(
	action,
	NegativeAction.GO_BACK,
	onShowLoading,
	onHideLoading,
	scope,
	onError,
	onSuccess
)

/**
 * @see executeRetryAbleActionOrGoBack
 */
fun <T : BaseResponse> BaseFragment<*>.executeRetryAbleActionOrCancel(
	action: suspend () -> Result<T>,
	scope: CoroutineScope = lifecycleScope,
	onShowLoading: (() -> Unit)? = null,
	onHideLoading: (() -> Unit)? = null,
	onError: (Result<T>) -> Unit = {},
	onSuccess: (T?) -> Unit,
): Job = executeRetryAbleActionOrEitherCancelOrGoBack(
	action,
	NegativeAction.CANCEL,
	onShowLoading,
	onHideLoading,
	scope,
	onError,
	onSuccess
)

private fun <T : BaseResponse> BaseFragment<*>.executeRetryAbleActionOrEitherCancelOrGoBack(
	action: suspend () -> Result<T>,
	negativeAction: NegativeAction,
	onShowLoading: (() -> Unit)? = null,
	onHideLoading: (() -> Unit)? = null,
	scope: CoroutineScope,
	onError: (Result<T>) -> Unit,
	onSuccess: (T?) -> Unit,
): Job {
	val hideLoading: () -> Unit = {
		if (onHideLoading == null) {
			dismissGlobalLoadingDialog()
		}else {
			onHideLoading()
		}
	}

	val showLoading: (CoroutineScope) -> Unit = {
		if (onShowLoading == null) {
			showGlobalLoadingDialog {
				it.cancel()

				context?.toast(getString(R.string.you_cancelled_the_action_successfully))

				when (negativeAction) {
					NegativeAction.CANCEL -> {
						hideLoading()
					}
					NegativeAction.GO_BACK -> {
						hideLoading()

						goBackIfPossible()
					}
				}
			}
		}else {
			onShowLoading()
		}
	}

	val onErrorAdditionalImpl: (Result<T>) -> Unit = { result ->
		Timber.w("error in executeRetryAbleActionOrEitherCancelOrGoBack -> ${result.exceptionOrNull()?.message}")

		val msg = context?.let { context ->
			when (val exception = result.exceptionOrNull()) {
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
								if (context.isNetworkAvailable()) {
									getString(R.string.unknown_connection_problem)
								}else {
									getString(R.string.no_internet_connection)
								}
							}
							ApiException.Reason.OTHER -> {
								context.getString(R.string.something_went_wrong_please_try_again)
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

		when (negativeAction) {
			NegativeAction.CANCEL -> {
				showGlobalErrorHandlingDialogOrCancel(
					msg,
					onCancelClick = {
						onError(result)
					}
				) {
					executeRetryAbleActionOrEitherCancelOrGoBack(
						action, negativeAction, onShowLoading, onHideLoading, scope, onError, onSuccess
					)
				}
			}
			NegativeAction.GO_BACK -> {
				showGlobalErrorHandlingDialogOrGoBack(
					msg,
					onGoBackClick = {
						onError(result)
					}
				) {
					executeRetryAbleActionOrEitherCancelOrGoBack(
						action, negativeAction, onShowLoading, onHideLoading, scope, onError, onSuccess
					)
				}
			}
		}
	}

	return executeRetryAbleActionGeneral(
		showLoading, hideLoading, scope, action, onErrorAdditionalImpl, onSuccess
	)
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
