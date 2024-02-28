package my.ym.taskcurrencyexchange.extensions

import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.helperTypes.BaseActivity
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment

fun BaseFragment<*>?.dismissGlobalLoadingDialog() {
	(this?.activity as? BaseActivity<*>)?.dismissGlobalLoadingDialog()
}

private fun BaseFragment<*>?.dismissGlobalErrorHandlingDialog() {
	(this?.activity as? BaseActivity<*>)?.dismissGlobalErrorHandlingDialog()
}

fun BaseFragment<*>?.showGlobalLoadingDialog(
	onBackPressed: () -> Unit
) {
	(this?.activity as? BaseActivity<*>)?.showGlobalLoadingDialog(onBackPressed)
}

private fun BaseFragment<*>?.showGlobalErrorHandlingDialog(
	msg: String,
	negativeButton: String,
	negativeButtonAction: () -> Unit,
	onRetry: () -> Unit
) {
	(this?.activity as? BaseActivity<*>)?.showGlobalErrorHandlingDialog(
		msg, negativeButton, negativeButtonAction, onRetry
	)
}

fun BaseFragment<*>?.showGlobalErrorHandlingDialogOrCancel(
	msg: String,
	onCancelClick: () -> Unit = {},
	onRetry: () -> Unit
) {
	if (this != null) {
		showGlobalErrorHandlingDialog(
			msg,
			getString(R.string.cancel),
			onCancelClick,
			onRetry
		)
	}
}

fun BaseFragment<*>?.showGlobalErrorHandlingDialogOrGoBack(
	msg: String,
	onGoBackClick: () -> Unit = {},
	onRetry: () -> Unit
) {
	if (this != null) {
		showGlobalErrorHandlingDialog(
			msg,
			getString(R.string.back),
			negativeButtonAction = {
				onGoBackClick()

				goBackIfPossible()
			},
			onRetry
		)
	}
}
