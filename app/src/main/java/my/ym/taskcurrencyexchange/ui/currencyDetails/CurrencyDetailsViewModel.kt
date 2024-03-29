package my.ym.taskcurrencyexchange.ui.currencyDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.domain.currenciesConversion.ConvertToSeveralCurrenciesForLastThreeDaysUseCase
import my.ym.taskcurrencyexchange.extensions.convertToFormatYYYYMMDD
import my.ym.taskcurrencyexchange.extensions.myApp
import my.ym.taskcurrencyexchange.extensions.orZero
import my.ym.taskcurrencyexchange.extensions.toIntIfNoFractionsOrThis
import my.ym.taskcurrencyexchange.helperTypes.BaseAndroidViewModel
import my.ym.taskcurrencyexchange.helperTypes.CurrencyUtils
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailsViewModel @Inject constructor(
	application: Application,
	private val args: CurrencyDetailsFragmentArgs,
	private val convertToSeveralCurrenciesForLastThreeDaysUseCase: ConvertToSeveralCurrenciesForLastThreeDaysUseCase,
) : BaseAndroidViewModel(application) {

	val title = MutableLiveData(application.getString(R.string.loading_ellipsis))

	/** Ex. list of -> Today - 50 */
	val lastThreeDaysData = MutableLiveData<List<Pair<String, Double>>?>(null)

	/** Ex. list of -> 200 USD */
	val otherCurrencies = MutableLiveData<List<Pair<String, Double>>?>(null)

	private val currenciesPopular = CurrencyUtils.topPopular11Currencies()

	fun fetchRatesForCurrencies() {
		executeRetryAbleActionOrGoBack(
			action = {
				convertToSeveralCurrenciesForLastThreeDaysUseCase(
					args.baseCurrency,
					args.baseValue.toDoubleOrNull(),
					args.targetCurrency,
					args.targetValue.toDoubleOrNull(),
					buildList {
						add(args.targetCurrency)

						addAll(currenciesPopular)
					}.distinct()
				)
			}
		) { response ->
			if (response == null) return@executeRetryAbleActionOrGoBack

			val baseValue = response.baseValue.orZero()

			title.value = myApp.getString(
				R.string.conversion_of_var_var_to_var,
				baseValue.toIntIfNoFractionsOrThis().toString(),
				args.baseCurrency,
				args.targetCurrency
			)

			lastThreeDaysData.value = response.rates?.map {
				val key = when (it.first) {
					LocalDate.now() -> myApp.getString(R.string.today)
					LocalDate.now().minusDays(1) -> myApp.getString(R.string.yesterday)
					else -> it.first.convertToFormatYYYYMMDD()
				}

				Pair(
					key,
					it.second[args.targetCurrency].orZero()
				)
			}

			// Since they are sorted, So first would be Today, another solution would be
			// response.rates?.firstOrNull { it.first == LocalDate.now() }
			// However I think it might have an error in a rare case, where request is done right
			// before today, and while loading from api, we are in next day a window of seconds isa,
			// However if can't rely on sorting you can cache today before calling the api isa.
			otherCurrencies.value = response.rates?.firstOrNull()?.let {
				buildList {
					for (currency in (currenciesPopular - args.targetCurrency).take(10)) {
						this += Pair(
							currency,
							it.second[currency].orZero()
						)
					}
				}
			}
		}
	}

}
