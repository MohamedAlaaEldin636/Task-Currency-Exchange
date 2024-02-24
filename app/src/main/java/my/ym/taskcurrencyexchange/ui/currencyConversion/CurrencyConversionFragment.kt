package my.ym.taskcurrencyexchange.ui.currencyConversion

import dagger.hilt.android.AndroidEntryPoint
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.databinding.FragmentCurrencyConversionBinding
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment

@AndroidEntryPoint
class CurrencyConversionFragment : BaseFragment<FragmentCurrencyConversionBinding>() {

	override fun getLayoutRes(): Int = R.layout.fragment_currency_conversion

	override fun setBindingVariables() {
		// Ex. add viewModel here isa.
		//binding
	}

}
