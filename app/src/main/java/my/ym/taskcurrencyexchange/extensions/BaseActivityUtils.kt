package my.ym.taskcurrencyexchange.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.databinding.DialogGlobalLoadingBinding
import my.ym.taskcurrencyexchange.databinding.DialogRetryErrorHandlingBinding
import my.ym.taskcurrencyexchange.helperTypes.BaseActivity
import timber.log.Timber
import kotlin.math.roundToInt

private var dialogGlobalLoading: Dialog? = null

private var dialogGlobalErrorHandling: Dialog? = null

/**
 * - Suppress is intended, Yes I don't use `receiver` yet I wanna restrict the function only to [BaseActivity] isa.
 *
 * - Another approach was to use [dialogGlobalLoading] as a global variable inside [BaseActivity],
 * However this will make it public leading to error prone of changes, While here they can only be changed
 * be below functions, Ex. [dismissGlobalLoadingDialog], [showGlobalLoadingDialog] etc...
 */
@Suppress("UnusedReceiverParameter")
fun BaseActivity<*>?.dismissGlobalLoadingDialog() {
	dialogGlobalLoading?.dismissSafely()

	dialogGlobalLoading = null
}

/**
 * @see [dismissGlobalLoadingDialog]
 */
@Suppress("UnusedReceiverParameter")
fun BaseActivity<*>?.dismissGlobalErrorHandlingDialog() {
	dialogGlobalErrorHandling?.dismissSafely()

	dialogGlobalErrorHandling = null
}

fun BaseActivity<*>?.showGlobalLoadingDialog(
	onBackPressed: () -> Unit
) {
	if (this == null || isFinishing || dialogGlobalLoading?.isShowing == true) {
		return
	}

	dialogGlobalLoading = Dialog(this)
	if (dialogGlobalLoading?.window != null) {
		dialogGlobalLoading?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
	}
	val binding = DataBindingUtil.inflate<DialogGlobalLoadingBinding>(
		layoutInflater, R.layout.dialog_global_loading, null, false
	)

	dialogGlobalLoading?.setContentView(binding.root)

	dialogGlobalLoading?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
	dialogGlobalLoading?.window?.setLayout(
		ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.MATCH_PARENT
	)

	dialogGlobalLoading?.setOnKeyListener { _, keyCode, _ ->
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed()

			true
		}else {
			false
		}
	}
	dialogGlobalLoading?.setCancelable(false)
	dialogGlobalLoading?.setCanceledOnTouchOutside(false)
	dialogGlobalLoading?.showSafely()
}

fun BaseActivity<*>?.showGlobalErrorHandlingDialog(
	msg: String,
	negativeButton: String,
	negativeButtonAction: () -> Unit,
	onRetry: () -> Unit
) {
	if (this == null || isFinishing || dialogGlobalErrorHandling?.isShowing == true) {
		return
	}

	dialogGlobalErrorHandling = Dialog(this)
	if (dialogGlobalErrorHandling?.window != null) {
		dialogGlobalErrorHandling?.window?.setBackgroundDrawable(
			InsetDrawable(
				ContextCompat.getDrawable(this, R.drawable.round_rect_16) ?: ColorDrawable(Color.WHITE),
				dpToPx(16f).roundToInt()
			)
		)
	}
	val binding = DataBindingUtil.inflate<DialogRetryErrorHandlingBinding>(
		layoutInflater, R.layout.dialog_retry_error_handling, null, false
	)
	binding.msgTextView.text = msg
	binding.negativeButton.text = negativeButton
	binding.negativeButton.setOnClickListener {
		negativeButtonAction()
	}
	binding.positiveButton.setOnClickListener {
		dismissGlobalErrorHandlingDialog()
		onRetry()
	}

	dialogGlobalErrorHandling?.setContentView(binding.root)

	dialogGlobalErrorHandling?.window?.setBackgroundDrawable(
		InsetDrawable(
			ContextCompat.getDrawable(this, R.drawable.round_rect_16) ?: ColorDrawable(Color.WHITE),
			dpToPx(16f).roundToInt()
		)
	)
	dialogGlobalErrorHandling?.window?.setLayout(
		ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.WRAP_CONTENT
	)

	dialogGlobalErrorHandling?.setOnKeyListener { _, keyCode, _ ->
		// Do nothing Just used to prevent back press isa.
		keyCode == KeyEvent.KEYCODE_BACK
	}
	dialogGlobalErrorHandling?.setCancelable(false)
	dialogGlobalErrorHandling?.setCanceledOnTouchOutside(false)
	dialogGlobalErrorHandling?.showSafely()
}

fun Dialog.showSafely() {
	kotlin.runCatching { show() }.getOrElse {
		Timber.e("Dialog.show Safely -> $it")
	}
}

fun Dialog.dismissSafely() {
	kotlin.runCatching { dismiss() }.getOrElse {
		Timber.e("Dialog.dismiss Safely -> $it")
	}
}
