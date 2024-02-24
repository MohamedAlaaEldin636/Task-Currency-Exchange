package my.ym.taskcurrencyexchange.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
