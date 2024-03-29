package my.ym.taskcurrencyexchange.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.getSystemService

fun Context.isNetworkAvailable(): Boolean {
	val connectivityManager = getSystemService<ConnectivityManager>() ?: return false

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		val network = connectivityManager.activeNetwork ?: return false
		val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
		return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
	} else {
		val networkInfo = connectivityManager.activeNetworkInfo
		@Suppress("DEPRECATION")
		return networkInfo?.isConnected ?: false
	}
}

/** - Layout inflater from `receiver`, by using [LayoutInflater.from] */
val Context.layoutInflater: LayoutInflater
	get() = LayoutInflater.from(this)

/**
 * - Inflates a layout from [layoutRes] isa.
 *
 * @param parent provide [ViewGroup.LayoutParams] to the returned root view, default is `null`
 * @param attachToRoot if true then the returned view will be attached to [parent] if not `null`,
 * default is false isa.
 *
 * @return rootView in the provided [layoutRes] isa.
 */
@JvmOverloads
fun Context.inflateLayout(
	@LayoutRes layoutRes: Int,
	parent: ViewGroup? = null,
	attachToRoot: Boolean = false
): View {
	return layoutInflater.inflate(layoutRes, parent, attachToRoot)
}
