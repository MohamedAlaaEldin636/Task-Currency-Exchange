package my.ym.taskcurrencyexchange.extensions

import android.content.Context
import androidx.core.util.TypedValueCompat

fun Context.dpToPx(value: Float): Float {
	return TypedValueCompat.dpToPx(value, resources.displayMetrics)
}
