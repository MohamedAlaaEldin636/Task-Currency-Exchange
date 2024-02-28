package my.ym.taskcurrencyexchange.ui.currencyConversion

import my.ym.taskcurrencyexchange.MyApp
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import my.ym.taskcurrencyexchange.data.FakeRepoImplConversions
import org.junit.Before
import my.ym.taskcurrencyexchange.data.FakeRepoImplSymbols
import org.junit.Test
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion
import org.junit.Assert.assertEquals

@RunWith(RobolectricTestRunner::class)
class TestCurrencyConversionViewModel {

	private lateinit var myApp: MyApp
	private lateinit var viewModel: CurrencyConversionViewModel

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
	}

	@Test
	fun fetchAllCurrencies() {
		assert(viewModel.currenciesSymbols.isEmpty())

		viewModel.fetchAllCurrencies {
			assert(viewModel.currenciesSymbols.isNotEmpty())
		}
	}

	@Test
	fun conversionSameCurrency() {
		val baseValue = 1.0

		viewModel.twoCurrenciesConversion.value = TwoCurrenciesConversion(
			FakeRepoImplSymbols.CURRENCY_EGP,
			baseValue,
			FakeRepoImplSymbols.CURRENCY_EGP,
			0.0,
		)

		viewModel.calculateConversionChange(true)

		val result = FakeRepoImplSymbols.getRateOfConversion(
			FakeRepoImplSymbols.CURRENCY_EGP,
			FakeRepoImplSymbols.CURRENCY_EGP,
		).times(baseValue)

		assertEquals(result, viewModel.twoCurrenciesConversion.value?.targetValue)
	}

	@Test
	fun conversionDifferentCurrency() {
		val baseValue = 1.0

		viewModel.twoCurrenciesConversion.value = TwoCurrenciesConversion(
			FakeRepoImplSymbols.CURRENCY_EGP,
			baseValue,
			FakeRepoImplSymbols.CURRENCY_USD,
			0.0,
		)

		viewModel.calculateConversionChange(true, ignoreDelay = true)

		val result = FakeRepoImplSymbols.getRateOfConversion(
			FakeRepoImplSymbols.CURRENCY_EGP,
			FakeRepoImplSymbols.CURRENCY_USD,
		).times(baseValue)

		assertEquals(result, viewModel.twoCurrenciesConversion.value?.targetValue)
	}

}
