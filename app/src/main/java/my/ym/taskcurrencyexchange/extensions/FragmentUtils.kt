package my.ym.taskcurrencyexchange.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.goBackIfPossible() {
	val navController = findNavControllerOrNull()
	when {
		navController != null -> navController.navigateUp()
		this is DialogFragment -> dismiss()
		else -> activity?.onBackPressedDispatcher?.onBackPressed()
	}
}
