package my.ym.taskcurrencyexchange.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.findNavControllerOrNull(): NavController? {
	return try {
		findNavController()
	}catch (e: IllegalStateException) {
		null
	}
}
