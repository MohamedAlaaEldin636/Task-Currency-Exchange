package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.extensions.LocalDateTimeUtils
import my.ym.taskcurrencyexchange.extensions.orZero
import my.ym.taskcurrencyexchange.models.ResponseMultiConversionsSeveralDays
import my.ym.taskcurrencyexchange.models.applySuccess
import timber.log.Timber
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
		Timber.e("dwdew ch 1")

		if (baseValue == null && targetValue == null) {
			throw RuntimeException("Both baseValue & targetValue can't be null, only 1 or none of them isa.")
		}

		Timber.e("dwdew ch 2")

		// If target value is `null` it's ok as it will be calculated anyway after fetching the rates isa.
		val actualBaseValue = if (baseValue != null) {
			Timber.e("dwdew ch 3")

			baseValue
		}else {
			Timber.e("dwdew ch 4")

			val result = repoConversions.convertCurrencyValue(
				targetCurrency,
				baseCurrency,
				targetValue.orZero()
			)

			result.getOrNull()?.result ?: return Result.failure(
				result.exceptionOrNull() ?: RuntimeException("Unexpected code 1-RE-U1")
			)
		}

		Timber.e("dwdew ch 5 $actualBaseValue")

		val result = getRatesOfCurrenciesForLastNDays(
			baseCurrency,
			targetCurrencies,
			3
		)

		Timber.e("dwdew ch 6 ${result.getOrNull()}")

		return if (result.isFailure) {
			Timber.e("dwdew ch 7")

			Result.failure(
				result.exceptionOrNull() ?: RuntimeException("Unexpected code 1-RE-U2")
			)
		}else {
			Timber.e("dwdew ch 8")

			result.getOrNull()?.let { response ->
				val rates = buildList {
					for ((key, value) in response.rates.orEmpty()) {
						val localDate = LocalDateTimeUtils.convertFromFormatYYYYMMDD(key) ?: return Result.failure(
							RuntimeException("Format to LocalDate issue Code 11-A")
						)

						this += localDate to value
					}
				}.sortedByDescending {
					it.first
				}

				Timber.e("dwdew ch 9 $rates")

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
