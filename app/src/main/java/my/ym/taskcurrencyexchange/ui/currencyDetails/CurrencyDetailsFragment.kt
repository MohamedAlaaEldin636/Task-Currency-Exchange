package my.ym.taskcurrencyexchange.ui.currencyDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import dagger.hilt.android.AndroidEntryPoint
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.databinding.FragmentCurrencyDetailsBinding
import my.ym.taskcurrencyexchange.extensions.navDeepLinkWithOptionsSlidingToFragment
import my.ym.taskcurrencyexchange.extensions.toIntIfNoFractionsOrThis
import my.ym.taskcurrencyexchange.helperTypes.BaseFragment
import my.ym.taskcurrencyexchange.models.TwoCurrenciesConversion
import my.ym.taskcurrencyexchange.ui.commonAdapters.RVItemTextScrollable

@AndroidEntryPoint
class CurrencyDetailsFragment : BaseFragment<FragmentCurrencyDetailsBinding>() {

	companion object {
		fun launch(navController: NavController, twoCurrenciesConversion: TwoCurrenciesConversion) {
			// Note usage of .toString() on a nullable receiver is intended since passing empty
			// excludes that parameter in the navigation isa.

			navController.navDeepLinkWithOptionsSlidingToFragment(
				"my.ym.taskcurrencyexchange.currency.details",
				paths = arrayOf(
					twoCurrenciesConversion.baseCurrency,
					twoCurrenciesConversion.baseValue.toString(),
					twoCurrenciesConversion.targetCurrency,
					twoCurrenciesConversion.targetValue.toString(),
				)
			)
		}
	}

	private val viewModel by viewModels<CurrencyDetailsViewModel>()

	private val adapterLastDays = RVItemTextScrollable()

	override fun getLayoutRes(): Int = R.layout.fragment_currency_details

	override fun setBindingVariables() {
		binding?.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		// Setup views
		binding?.recyclerViewLastDays?.layoutManager = GridLayoutManager(
			requireContext(),
			2,
			GridLayoutManager.VERTICAL,
			false
		)
		binding?.recyclerViewLastDays?.adapter = adapterLastDays

		// Fetch data
		viewModel.fetchRatesForCurrencies(this)

		// Observe Live data
		viewModel.lastThreeDaysData.observe(viewLifecycleOwner) {
			if (it.isNullOrEmpty().not()) {
				val list = buildList {
					for (item in it.orEmpty()) {
						add(item.first)
						add(item.second.toIntIfNoFractionsOrThis().toString())
					}
				}
				adapterLastDays.submitList(list)

				setupChart(it.orEmpty())
			}
		}
	}

	private fun setupChart(listOfData: List<Pair<String, Double>>) {
		binding?.apply {
			anyChartView.setProgressBar(progressBar)

			val keyOfKey = "x"
			val keyOfValue = "value"
			val pie = AnyChart.pie()
			pie.setOnClickListener(
				object : ListenersInterface.OnClickListener(arrayOf(keyOfKey, keyOfValue)) {
					override fun onClick(event: Event) {
						// x -> Ex. Today -> Key
						// value -> Ex. 30 -> Value
						/*val key = event.data[keyOfKey]
						val value = event.data[keyOfValue]

						Timber.d("Key $key, Value $value, Other data -> ${event.data}")*/
					}
				}
			)

			val data = buildList {
				listOfData.forEach { (key , value) ->
					add(ValueDataEntry(key, value))
				}
			}
			pie.data(data)

			//pie.labels()
			//pie.explode()

			//pie.title("Fruits imported in 2015 (in kg)");
			pie.title().enabled(false)

			pie.labels().position("outside")

			pie.legend().title().enabled(true)
			pie.legend().title()
				.text("Last 3 Days")
				.padding(0, 0, 10, 0)

			pie.legend()
				.position("center-bottom")
				.itemsLayout(LegendLayout.HORIZONTAL)
				.align(Align.CENTER)

			anyChartView.setChart(pie)
		}
	}

}
