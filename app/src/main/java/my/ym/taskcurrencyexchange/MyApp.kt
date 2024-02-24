package my.ym.taskcurrencyexchange

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import my.ym.taskcurrencyexchange.helperTypes.TimberLineNumberDebugTree
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

	override fun onCreate() {
		super.onCreate()

		Timber.plant(TimberLineNumberDebugTree())
	}

}
