package my.ym.taskcurrencyexchange.ui.currencyConversion

import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import my.ym.taskcurrencyexchange.MyApp
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import my.ym.taskcurrencyexchange.data.FakeRepoImplConversions
import org.junit.Before
import my.ym.taskcurrencyexchange.data.FakeRepoImplSymbols
import org.junit.Test
import androidx.test.core.app.ActivityScenario.launch
import my.ym.taskcurrencyexchange.MainActivity
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion

@RunWith(RobolectricTestRunner::class)
class TestCurrencyConversionViewModel {

	private lateinit var myApp: MyApp
	private lateinit var viewModel: CurrencyConversionViewModel

	private lateinit var scenarioMainActivity: ActivityScenario<MainActivity>

	@Before
	fun setup() {
		myApp = getApplicationContext()

		val repoSymbols = FakeRepoImplSymbols.Success
		val repoConversions = FakeRepoImplConversions.Success
		viewModel = CurrencyConversionViewModel(
			myApp,
			repoSymbols,
			repoConversions
		)

		scenarioMainActivity = launch(MainActivity::class.java)
		scenarioMainActivity.moveToState(Lifecycle.State.RESUMED)
	}

	@Test
	fun fetchAllCurrencies() {
		onCurrencyConversionFragment {
			assert(viewModel.currenciesSymbols.isEmpty())

			viewModel.fetchAllCurrencies(it) {
				assert(viewModel.currenciesSymbols.isNotEmpty())
			}
		}
	}

	@Test
	fun conversionSameCurrency() {
		fetchAllCurrencies()

		onCurrencyConversionFragment {
			val baseValue = 1.0

			viewModel.twoCurrenciesConversion.value = TwoCurrenciesConversion(
				FakeRepoImplSymbols.CURRENCY_EGP,
				baseValue,
				FakeRepoImplSymbols.CURRENCY_EGP,
				0.0,
			)

			viewModel.calculateConversionChange(it, true)

			assert(viewModel.twoCurrenciesConversion.value?.targetValue == baseValue)
		}
	}

	@Test
	fun conversionDifferentCurrency() {
		fetchAllCurrencies()

		onCurrencyConversionFragment {
			val baseValue = 1.0

			viewModel.twoCurrenciesConversion.value = TwoCurrenciesConversion(
				FakeRepoImplSymbols.CURRENCY_EGP,
				baseValue,
				FakeRepoImplSymbols.CURRENCY_USD,
				0.0,
			)

			viewModel.calculateConversionChange(it, true)

			val result = FakeRepoImplSymbols.RATIO_CONVERSION_EGP_TO_USD.times(
				baseValue
			)

			assert(viewModel.twoCurrenciesConversion.value?.targetValue == result)
		}
	}

	private fun onCurrencyConversionFragment(action: (fragment: CurrencyConversionFragment) -> Unit) {
		scenarioMainActivity.onActivity { activity ->
			val navHostFragment = activity.supportFragmentManager
				.findFragmentById(R.id.navHostFragment) as? NavHostFragment

			val fragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull {
				it is CurrencyConversionFragment
			} as? CurrencyConversionFragment ?: error("Can't find fragment")

			action(fragment)
		}
	}

}
