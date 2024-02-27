package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.extensions.LocalDateTimeUtils
import my.ym.taskcurrencyexchange.extensions.orZero
import my.ym.taskcurrencyexchange.extensions.round
import my.ym.taskcurrencyexchange.models.ResponseMultiConversionsSeveralDays
import my.ym.taskcurrencyexchange.models.applySuccess
import javax.inject.Inject

class ConvertToSeveralCurrenciesForLastThreeDays @Inject constructor(
	private val getRatesOfCurrenciesForLastNDays: GetRatesOfCurrenciesForLastNDays,
	private val repoConversions: RepoConversions,
) {

	/**
	 * @throws RuntimeException in case both [baseValue] & [targetValue] are both `null` isa.
	 */
	suspend operator fun invoke(
		baseCurrency: String,
		baseValue: Double?,
		targetCurrency: String,
		targetValue: Double?,
		targetCurrencies: List<String>,
	): Result<ResponseMultiConversionsSeveralDays> {
		if (baseValue == null && targetValue == null) {
			throw RuntimeException("Both baseValue & targetValue can't be null, only 1 or none of them isa.")
		}

		// If target value is `null` it's ok as it will be calculated anyway after fetching the rates isa.
		val actualBaseValue = if (baseValue != null) {
			baseValue
		}else {
			val result = repoConversions.convertCurrencyValue(
				targetCurrency,
				baseCurrency,
				targetValue.orZero()
			)

			result.getOrNull()?.result ?: return Result.failure(
				result.exceptionOrNull() ?: RuntimeException("Unexpected code 1-RE-U1")
			)
		}

		val result = getRatesOfCurrenciesForLastNDays(
			baseCurrency,
			targetCurrencies,
			3
		)

		return if (result.isFailure) {
			Result.failure(
				result.exceptionOrNull() ?: RuntimeException("Unexpected code 1-RE-U2")
			)
		}else {
			result.getOrNull()?.let { response ->
				val rates = buildList {
					for ((key, value) in response.rates.orEmpty()) {
						val localDate = LocalDateTimeUtils.convertFromFormatYYYYMMDD(key) ?: return Result.failure(
							RuntimeException("Format to LocalDate issue Code 11-A")
						)

						this += Pair(
							localDate,
							value.mapValues { (_, doubleValue) ->
								doubleValue.times(actualBaseValue).round(6)
							}
						)
					}
				}.sortedByDescending {
					it.first
				}

				Result.success(
					ResponseMultiConversionsSeveralDays(
						baseCurrency,
						actualBaseValue,
						rates
					).applySuccess()
				)
			} ?: Result.failure(
				RuntimeException("Success not existent issue Code 11-B")
			)
		}
	}

}
