package my.ym.taskcurrencyexchange

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import my.ym.taskcurrencyexchange.databinding.ActivityMainBinding
import my.ym.taskcurrencyexchange.helperTypes.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

	override fun getLayoutRes(): Int = R.layout.activity_main

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Setup Navigation Component
		val navHostFragment = supportFragmentManager
			.findFragmentById(R.id.navHostFragment) as NavHostFragment
		val navController = navHostFragment.navController
		val appBarConfiguration = AppBarConfiguration(setOf(
			R.id.currencyConversionFragment
		))
		binding.materialToolbar.setupWithNavController(
			navController, appBarConfiguration
		)
	}

}