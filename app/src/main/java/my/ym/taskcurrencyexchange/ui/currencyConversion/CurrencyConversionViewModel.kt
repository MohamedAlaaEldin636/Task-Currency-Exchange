package my.ym.taskcurrencyexchange.ui.currencyConversion

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoConversions
import my.ym.taskcurrencyexchange.data.remote.currenciesSymbols.RepoSymbols
import my.ym.taskcurrencyexchange.databinding.FragmentCurrencyConversionBinding
import my.ym.taskcurrencyexchange.extensions.executeRetryAbleActionOrGoBack
import my.ym.taskcurrencyexchange.extensions.findBindingOrNull
import my.ym.taskcurrencyexchange.extensions.findFragmentOrNull
import my.ym.taskcurrencyexchange.extensions.orZero
import my.ym.taskcurrencyexchange.extensions.toIntIfNoFractionsOrThis
import my.ym.taskcurrencyexchange.extensions.toast
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
	application: Application,
	val repoSymbols: RepoSymbols,
	private val repoConversions: RepoConversions,
) : AndroidViewModel(application) {

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

	fun afterChange(view: View, changeType: CurrencyConversionFragment.ChangeType) {
		val binding = view.findBindingOrNull<FragmentCurrencyConversionBinding>() ?: return

		val fragment = view.findFragmentOrNull<CurrencyConversionFragment>() ?: return

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

		calculateConversionBasedOnBaseChange(fragment, isBaseChangedNotTarget)
	}

	fun swapTargetAndBaseCurrencies(view: View) {
		val fragment = view.findFragmentOrNull<CurrencyConversionFragment>() ?: return

		twoCurrenciesConversion.value = twoCurrenciesConversion.value?.let {
			it.copy(
				baseCurrency = it.targetCurrency,
				targetCurrency = it.baseCurrency,
			)
		}

		calculateConversionBasedOnBaseChange(
			fragment,
			true
		)
	}

	fun swapTargetAndBaseValues(view: View) {
		val fragment = view.findFragmentOrNull<CurrencyConversionFragment>() ?: return

		twoCurrenciesConversion.value = twoCurrenciesConversion.value?.let {
			it.copy(
				baseValue = it.targetValue,
			)
		}

		calculateConversionBasedOnBaseChange(
			fragment,
			true
		)
	}

	fun goToDetailsScreen(view: View) {
		view.context?.toast("Not yet implemented isa.")

		//TODO("Not yet implemented isa.")
	}

	/**
	 * - You should make changes to [twoCurrenciesConversion] before calling this.
	 *
	 * @param [isBaseChangedNotTarget] if `true` means you want to calculate based on base value,
	 * so you changed base value, Otherwise means you just changed target value and want to
	 * calculate corresponding base value isa.
	 */
	fun calculateConversionBasedOnBaseChange(
		fragment: CurrencyConversionFragment,
		isBaseChangedNotTarget: Boolean,
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
				fragment.executeRetryAbleActionOrGoBack(
					action = {
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
