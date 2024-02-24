package my.ym.taskcurrencyexchange.extensions

import android.content.Context
import android.widget.Toast

private var toast: Toast? = null

fun Context.toast(
	msg: CharSequence,
	duration: Int = Toast.LENGTH_SHORT
) {
	dismissToast()
	toast = Toast.makeText(this, msg, duration)
	toast?.show()
}

/**
 * - Suppress is intended to restrict the function `receiver` to [Context] isa.
 */
@Suppress("UnusedReceiverParameter")
fun Context.dismissToast() {
	toast?.cancel()
	toast = null
}
