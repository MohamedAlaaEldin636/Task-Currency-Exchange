package my.ym.taskcurrencyexchange.ui.currencyConversion

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.databinding.FragmentCurrencyConversionBinding
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment
import my.ym.taskcurrencyexchange.helperTypes.NonFilterArrayAdapter
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion

@AndroidEntryPoint
class CurrencyConversionFragment : BaseFragment<FragmentCurrencyConversionBinding>() {

	private val viewModel by viewModels<CurrencyConversionViewModel>()

	private var adapterBase: ArrayAdapter<String>? = null
	private var adapterTarget: ArrayAdapter<String>? = null

	override fun getLayoutRes(): Int = R.layout.fragment_currency_conversion

	override fun setBindingVariables() {
		binding?.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		adapterBase = NonFilterArrayAdapter(requireContext(), R.layout.item_in_list)
		binding?.baseCurrencyAutoCompleteTextView?.setAdapter(adapterBase)

		adapterTarget = NonFilterArrayAdapter(requireContext(), R.layout.item_in_list)
		binding?.targetCurrencyAutoCompleteTextView?.setAdapter(adapterTarget)

		// Get All Symbols
		if (viewModel.currenciesSymbols.isEmpty()) {
			viewModel.fetchAllCurrencies(this, ::performAfterGettingCurrencies)
		}else {
			performAfterGettingCurrencies()
		}
	}

	private fun performAfterGettingCurrencies() {
		adapterBase?.clear()
		adapterBase?.addAll(viewModel.currenciesSymbols)
		adapterBase?.notifyDataSetChanged()

		adapterTarget?.clear()
		adapterTarget?.addAll(viewModel.currenciesSymbols)

		if (viewModel.twoCurrenciesConversion.value == null) {
			viewModel.currenciesSymbols.firstOrNull()?.also {
				viewModel.twoCurrenciesConversion.value = TwoCurrenciesConversion(
					it,
					1.0,
					viewModel.currenciesSymbols.getOrNull(1) ?: it,
					0.0
				)
			}

			viewModel.calculateConversionBasedOnBaseChange(this, true)
		}
	}

	enum class ChangeType {
		BASE_CURRENCY,
		TARGET_CURRENCY,
		BASE_VALUE,
		TARGET_VALUE,
	}

}
