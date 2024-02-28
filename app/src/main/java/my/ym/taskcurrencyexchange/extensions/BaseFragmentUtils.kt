package my.ym.taskcurrencyexchange.extensions

import my.ym.taskcurrencyexchange.helperTypes.BaseActivity
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment

fun BaseFragment<*, *>?.dismissGlobalLoadingDialog() {
	(this?.activity as? BaseActivity<*>)?.dismissGlobalLoadingDialog()
}

fun BaseFragment<*, *>?.dismissGlobalErrorHandlingDialog() {
	(this?.activity as? BaseActivity<*>)?.dismissGlobalErrorHandlingDialog()
}

fun BaseFragment<*, *>?.showGlobalLoadingDialog(
	onBackPressed: () -> Unit
) {
	(this?.activity as? BaseActivity<*>)?.showGlobalLoadingDialog(onBackPressed)
}

fun BaseFragment<*, *>?.showGlobalErrorHandlingDialog(
	msg: String,
	negativeButton: String,
	negativeButtonAction: () -> Unit,
	onRetry: () -> Unit
) {
	(this?.activity as? BaseActivity<*>)?.showGlobalErrorHandlingDialog(
		msg, negativeButton, negativeButtonAction, onRetry
	)
}
