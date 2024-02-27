package my.ym.taskcurrencyexchange.extensions

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import my.ym.taskcurrencyexchange.R

private const val SCHEME_FRAGMENT = "fragment-dest"

object NavControllerUtils {

	fun createSlidingNavOptions(additionalAdjustments: NavOptions.Builder.() -> NavOptions.Builder = { this }): NavOptions {
		return NavOptions.Builder()
			.setEnterAnim(R.anim.slide_in_right)
			.setExitAnim(R.anim.slide_out_left)
			.setPopEnterAnim(R.anim.slide_in_left)
			.setPopExitAnim(R.anim.slide_out_right)
			.additionalAdjustments()
			.build()
	}

}

fun NavController.navDeepLinkWithOptionsSlidingToFragment(
	authority: String,
	options: NavOptions = NavControllerUtils.createSlidingNavOptions(),
	vararg paths: String
) = navDeepLinkWithOptionsToFragment(authority, options, *paths)

fun NavController.navDeepLinkWithOptions(scheme: String, authority: String, options: NavOptions, vararg paths: String) =
	navDeepLinkOptionalOptions(scheme, authority, options, *paths)

fun NavController.navDeepLinkWithOptionsToFragment(
	authority: String,
	options: NavOptions,
	vararg paths: String
) = navDeepLinkWithOptions(SCHEME_FRAGMENT, authority, options, *paths)

fun NavController.navDeepLinkWithoutOptions(scheme: String, authority: String, vararg paths: String) =
	navDeepLinkOptionalOptions(scheme, authority, null, *paths)

fun NavController.navDeepLinkWithoutOptionsToFragment(authority: String, vararg paths: String) =
	navDeepLinkWithoutOptions(SCHEME_FRAGMENT, authority, *paths)

private fun NavController.navDeepLinkOptionalOptions(
	scheme: String,
	authority: String,
	options: NavOptions?,
	vararg paths: String?
) {
	val uri = Uri.Builder()
		.scheme(scheme)
		.authority(authority)
		.let {
			var builder = it
			for (path in paths) {
				builder = builder.appendPath(path)
			}
			builder
		}
		.build()
	val request = NavDeepLinkRequest.Builder.fromUri(uri).build()

	navigate(request, options)
}

fun Fragment.findNavControllerOrNull(): NavController? {
	return try {
		findNavController()
	}catch (e: IllegalStateException) {
		null
	}
}
