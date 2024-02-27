package my.ym.taskcurrencyexchange.extensions

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle

/**
 * - Constructs a [Bundle] for every key/value pair in the `receiver` isa.
 */
fun SavedStateHandle.asBundle(): Bundle {
	val bundle = Bundle()
	for (key in keys()) {
		bundle.addValue(key, get(key))
	}
	
	return bundle
}

fun Bundle.asSavedStateHandle(): SavedStateHandle {
	val map = mutableMapOf<String, Any?>()
	for (key in keySet()) {
		map += key to get(key)
	}

	return SavedStateHandle()
}
