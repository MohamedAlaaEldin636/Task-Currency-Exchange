package my.ym.taskcurrencyexchange.models

import java.time.LocalDate

/**
 * @param rates [Pair.first] -> [LocalDate], Value is a [Map] with key of currency symbol
 * and value of the rate value isa.
 *
 * Ex. "2024-02-22": {
 * 		"USD": 0.032362,
 * 		"CAD": 0.043631,
 * 		"JPY": 4.870019
 * 	}
 */
data class ResponseMultiConversionsSeveralDays(
	val baseCurrency: String?,
	val baseValue: Double?,
	val rates: List<Pair<LocalDate, Map<String, Double>>>?,
) : BaseResponse()
