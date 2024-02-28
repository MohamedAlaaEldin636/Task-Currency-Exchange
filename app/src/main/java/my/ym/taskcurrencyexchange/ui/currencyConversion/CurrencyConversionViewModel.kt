package my.ym.taskcurrencyexchange.ui.currencyConversion

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoConversions
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoSymbols
import my.ym.taskcurrencyexchange.databinding.FragmentCurrencyConversionBinding
import my.ym.taskcurrencyexchange.extensions.findBindingOrNull
import my.ym.taskcurrencyexchange.extensions.orZero
import my.ym.taskcurrencyexchange.extensions.toIntIfNoFractionsOrThis
import my.ym.taskcurrencyexchange.extensions.toast
import my.ym.taskcurrencyexchange.helperTypes.BaseAndroidViewModel
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion
import my.ym.taskcurrencyexchange.models.orEmpty
import my.ym.taskcurrencyexchange.ui.currencyDetails.CurrencyDetailsFragment
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
	application: Application,
	private val repoSymbols: RepoSymbols,
	private val repoConversions: RepoConversions,
) : BaseAndroidViewModel(application) {

	var currenciesSymbols = emptyList<String>()

	/** Used below approach instead of a flag for change of both fields isa. */
	val twoCurrenciesConversion = MutableLiveData<TwoCurrenciesConversion?>(null)

	val baseCurrency = twoCurrenciesConversion.map {
		it?.baseCurrency.orEmpty()
	}
	val targetCurrency = twoCurrenciesConversion.map {
		it?.targetCurrency.orEmpty()
	}

	val baseValue = twoCurrenciesConversion.map {
		it?.baseValue?.toIntIfNoFractionsOrThis()?.toString().orEmpty()
	}
	val targetValue = twoCurrenciesConversion.map {
		it?.targetValue?.toIntIfNoFractionsOrThis()?.toString().orEmpty()
	}

	val showLoadingForBaseValue = MutableLiveData(false)
	val showLoadingForTargetValue = MutableLiveData(false)

	private var jobOfCalculateConversion: Job? = null

	fun fetchAllCurrencies(onSuccess: () -> Unit) {
		executeRetryAbleActionOrGoBack(
			action = {
				repoSymbols.getAllCurrenciesSymbols()
			}
		) { response ->
			currenciesSymbols = response?.symbols.orEmpty().keys.toList()

			onSuccess()
		}
	}

	fun afterChange(view: View, changeType: CurrencyConversionFragment.ChangeType) {
		val binding = view.findBindingOrNull<FragmentCurrencyConversionBinding>() ?: return

		var isBaseChangedNotTarget = true
		val newTwoCurrenciesConversion = when (changeType) {
			CurrencyConversionFragment.ChangeType.BASE_CURRENCY -> {
				val newValue = binding.baseCurrencyAutoCompleteTextView.text?.toString().orEmpty()

				if (newValue == twoCurrenciesConversion.value?.baseCurrency) {
					return
				}else {
					twoCurrenciesConversion.value?.copy(
						baseCurrency = newValue
					)
				}
			}
			CurrencyConversionFragment.ChangeType.TARGET_CURRENCY -> {
				val newValue = binding.targetCurrencyAutoCompleteTextView.text?.toString().orEmpty()

				if (newValue == twoCurrenciesConversion.value?.targetCurrency) {
					return
				}else {
					twoCurrenciesConversion.value?.copy(
						targetCurrency = newValue
					)
				}
			}
			CurrencyConversionFragment.ChangeType.BASE_VALUE -> {
				val newValue = binding.baseValueTextInputEditText.text?.toString()?.toDoubleOrNull()

				if (newValue == twoCurrenciesConversion.value?.baseValue) {
					return
				}else {
					twoCurrenciesConversion.value?.copy(
						baseValue = newValue
					)
				}
			}
			CurrencyConversionFragment.ChangeType.TARGET_VALUE -> {
				isBaseChangedNotTarget = false

				val newValue = binding.targetValueTextInputEditText.text?.toString()?.toDoubleOrNull()

				if (newValue == twoCurrenciesConversion.value?.targetValue) {
					return
				}else {
					twoCurrenciesConversion.value?.copy(
						targetValue = newValue
					)
				}
			}
		}

		twoCurrenciesConversion.value = newTwoCurrenciesConversion

		calculateConversionChange(isBaseChangedNotTarget)
	}

	fun swapTargetAndBaseCurrencies() {
		twoCurrenciesConversion.value = twoCurrenciesConversion.value?.let {
			it.copy(
				baseCurrency = it.targetCurrency,
				targetCurrency = it.baseCurrency,
			)
		}

		calculateConversionChange(true)
	}

	fun swapTargetAndBaseValues() {
		twoCurrenciesConversion.value = twoCurrenciesConversion.value?.let {
			it.copy(
				baseValue = it.targetValue,
			)
		}

		calculateConversionChange(true)
	}

	fun goToDetailsScreen(view: View) {
		val context = view.context ?: return

		if (twoCurrenciesConversion.value?.baseValue == null
			&& twoCurrenciesConversion.value?.targetValue == null) {
			view.context?.toast(context.getString(R.string.you_must_select_either_base_value_or_target_value))

			return
		}

		if (showLoadingForBaseValue.value == true || showLoadingForTargetValue.value == true) {
			jobOfCalculateConversion?.cancel()
		}

		CurrencyDetailsFragment.launch(
			view.findNavController(),
			twoCurrenciesConversion.value.orEmpty()
		)
	}

	/**
	 * - VIP Note, I know I can just use the ratio API and then use math in case of no change for
	 * the currencies & even add caching in case returned to same currencies, However since
	 * currency conversion is sensitive and might change in any second it's better to keep
	 * calling api even on a value change without currency one isa.
	 *
	 * - You should make changes to [twoCurrenciesConversion] before calling this.
	 *
	 * @param [isBaseChangedNotTarget] if `true` means you want to calculate based on base value,
	 * so you changed base value, Otherwise means you just changed target value and want to
	 * calculate corresponding base value isa.
	 */
	fun calculateConversionChange(
		isBaseChangedNotTarget: Boolean,
		ignoreDelay: Boolean = false
	) {
		val twoCurrenciesConversion = twoCurrenciesConversion.value

		val insufficientInputData = twoCurrenciesConversion?.baseCurrency.isNullOrEmpty()
				|| twoCurrenciesConversion?.targetCurrency.isNullOrEmpty()

		when {
			twoCurrenciesConversion == null || insufficientInputData -> {
				this.twoCurrenciesConversion.value = null

				return
			}
			isBaseChangedNotTarget && twoCurrenciesConversion.baseValue == null -> {
				this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
					targetValue = null
				)
			}
			isBaseChangedNotTarget.not() && twoCurrenciesConversion.targetValue == null -> {
				this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
					baseValue = null
				)
			}
			twoCurrenciesConversion.baseCurrency == twoCurrenciesConversion.targetCurrency -> {
				if (isBaseChangedNotTarget) {
					this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
						targetValue = twoCurrenciesConversion.baseValue
					)
				}else {
					this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
						baseValue = twoCurrenciesConversion.targetValue
					)
				}
			}
			else -> {
				// Used below approach to facilitate changes for user instead of blocking him/her
				// for each edit of a digit
				jobOfCalculateConversion?.cancel()
				jobOfCalculateConversion = executeRetryAbleActionOrGoBack(
					onShowLoading = {
						if (isBaseChangedNotTarget) {
							showLoadingForTargetValue.value = true
						}else {
							showLoadingForBaseValue.value = true
						}
					},
					onHideLoading = {
						if (isBaseChangedNotTarget) {
							showLoadingForTargetValue.postValue(false)
						}else {
							showLoadingForBaseValue.postValue(false)
						}
					},
					action = {
						// wait small time in case multiple calls so we don't call api multiple
						// times unnecessarily
						if (ignoreDelay.not()) {
							delay(300)
						}

						if (isBaseChangedNotTarget) {
							repoConversions.convertCurrencyValue(
								twoCurrenciesConversion.baseCurrency,
								twoCurrenciesConversion.targetCurrency,
								twoCurrenciesConversion.baseValue.orZero(),
							)
						}else {
							// Note change of from currency as only on change of target value
							// we convert from target to base isa.
							repoConversions.convertCurrencyValue(
								twoCurrenciesConversion.targetCurrency,
								twoCurrenciesConversion.baseCurrency,
								twoCurrenciesConversion.targetValue.orZero(),
							)
						}
					}
				) {
					it?.result.orZero().also { result ->
						if (isBaseChangedNotTarget) {
							this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
								targetValue = result
							)
						}else {
							this.twoCurrenciesConversion.value = twoCurrenciesConversion.copy(
								baseValue = result
							)
						}
					}
				}
			}
		}

	}

}
