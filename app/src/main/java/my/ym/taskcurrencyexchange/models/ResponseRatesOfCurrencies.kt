package my.ym.taskcurrencyexchange.models

import com.google.gson.annotations.SerializedName

/**
 * @param startDate YYYY-MM-DD use 02 in case of was trying to use 2 isa.
 *
 * @param rates key -> same format as [startDate], Value is a [Map] with key of currency symbol
 * and value of the rate value isa.
 *
 * Ex. "2024-02-22": {
 * 		"USD": 0.032362,
 * 		"CAD": 0.043631,
 * 		"JPY": 4.870019
 * 	}
 */
data class ResponseRatesOfCurrencies(
	@SerializedName("start_date") val startDate: String?,
	@SerializedName("end_date") val endDate: String?,
	@SerializedName("base") val baseCurrency: String?,
	val rates: Map<String, Map<String, Double>>?,
) : BaseResponse()
