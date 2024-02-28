package my.ym.taskcurrencyexchange.ui.currencyDetails

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import my.ym.taskcurrencyexchange.MyApp
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import my.ym.taskcurrencyexchange.MainActivity
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.data.FakeRepoImplConversions
import org.junit.Before
import my.ym.taskcurrencyexchange.data.FakeRepoImplSymbols
import org.junit.Test
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.ConvertToSeveralCurrenciesForLastThreeDaysUseCase
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.GetRatesOfCurrenciesForLastNDaysUseCase
import my.ym.taskcurrencyexchange.extensions.navDeepLinkWithOptionsSlidingToFragment
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion
import my.ym.taskcurrencyexchange.ui.currencyConversion.CurrencyConversionFragment
import org.junit.Assert.assertEquals

@RunWith(RobolectricTestRunner::class)
class TestCurrencyDetailsViewModel {

	private lateinit var myApp: MyApp

	private lateinit var convertToSeveralCurrenciesForLastThreeDaysUseCase: ConvertToSeveralCurrenciesForLastThreeDaysUseCase

	@Before
	fun setup() {
		myApp = getApplicationContext()

		val repoConversions = FakeRepoImplConversions.Success
		val getRatesOfCurrenciesForLastNDaysUseCase = GetRatesOfCurrenciesForLastNDaysUseCase(repoConversions)
		convertToSeveralCurrenciesForLastThreeDaysUseCase = ConvertToSeveralCurrenciesForLastThreeDaysUseCase(
			getRatesOfCurrenciesForLastNDaysUseCase,
			repoConversions
		)
	}

	@Test
	fun conversionWithoutTargetValue() = runTest {
		val baseCurrency = FakeRepoImplSymbols.CURRENCY_EGP
		val baseValue = 2.0
		val targetCurrency = FakeRepoImplSymbols.CURRENCY_USD

		val viewModel = CurrencyDetailsViewModel(
			myApp,
			CurrencyDetailsFragmentArgs(
				baseCurrency,
				baseValue.toString(),
				targetCurrency,
				""
			),
			convertToSeveralCurrenciesForLastThreeDaysUseCase
		)

		viewModel.fetchRatesForCurrencies()

		val response = convertToSeveralCurrenciesForLastThreeDaysUseCase(
			baseCurrency,
			baseValue,
			targetCurrency,
			null,
			listOf(targetCurrency)
		).getOrNull()!!

		assertEquals(response.rates?.size, 3)

		assertEquals(viewModel.lastThreeDaysData.value?.size, 3)

		val expectedConvertedValue = FakeRepoImplSymbols.getRateOfConversion(
			baseCurrency, targetCurrency
		).times(baseValue)
		val convertedValue = viewModel.lastThreeDaysData.value.orEmpty().first().second
		assertEquals(expectedConvertedValue, convertedValue, 0.0)
	}

	@Test
	fun conversionWithoutBaseValue() = runTest {
		val baseCurrency = FakeRepoImplSymbols.CURRENCY_EGP
		val targetCurrency = FakeRepoImplSymbols.CURRENCY_USD
		val targetValue = 4.0

		val viewModel = CurrencyDetailsViewModel(
			myApp,
			CurrencyDetailsFragmentArgs(
				baseCurrency,
				"",
				targetCurrency,
				targetValue.toString()
			),
			convertToSeveralCurrenciesForLastThreeDaysUseCase
		)

		viewModel.fetchRatesForCurrencies()

		val response = convertToSeveralCurrenciesForLastThreeDaysUseCase(
			baseCurrency,
			null,
			targetCurrency,
			targetValue,
			listOf(targetCurrency)
		).getOrNull()!!

		assertEquals(
			response.baseValue,
			FakeRepoImplSymbols.getRateOfConversion(
				targetCurrency, baseCurrency
			).times(targetValue)
		)

		assertEquals(response.rates?.size, 3)

		assertEquals(viewModel.lastThreeDaysData.value?.size, 3)

		val convertedValue = viewModel.lastThreeDaysData.value.orEmpty().first().second
		assertEquals(targetValue, convertedValue, 0.0)
	}

}
