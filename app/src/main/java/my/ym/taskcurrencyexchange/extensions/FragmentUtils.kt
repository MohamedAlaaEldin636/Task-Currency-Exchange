package my.ym.taskcurrencyexchange.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment

fun Fragment.goBackIfPossible() {
	activity?.onBackPressedDispatcher?.onBackPressed()
}

inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
	return try {
		findFragment()
	}catch (e: IllegalStateException) {
		null
	}
}
