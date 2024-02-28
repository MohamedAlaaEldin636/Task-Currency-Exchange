package my.ym.taskcurrencyexchange.data

import my.ym.taskcurrencyexchange.data.remote.currenciesConversion.RepoConversions
import my.ym.taskcurrencyexchange.extensions.convertToFormatYYYYMMDD
import my.ym.taskcurrencyexchange.models.ResponseConversion
import my.ym.taskcurrencyexchange.models.ResponseRatesOfCurrencies
import java.time.LocalDate
import java.time.Period

object FakeRepoImplConversions {

	object Success : RepoConversions {

		override suspend fun convertCurrencyValue(
			fromCurrency: String,
			toCurrency: String,
			amountToConvert: Double
		): Result<ResponseConversion> {
			return when {
				fromCurrency == FakeRepoImplSymbols.CURRENCY_EGP
					&& toCurrency == FakeRepoImplSymbols.CURRENCY_EGP -> {
						Result.success(
							ResponseConversion(
								amountToConvert,
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == FakeRepoImplSymbols.CURRENCY_EGP
					&& toCurrency == FakeRepoImplSymbols.CURRENCY_USD -> {
						Result.success(
							ResponseConversion(
								amountToConvert.times(FakeRepoImplSymbols.RATIO_CONVERSION_EGP_TO_USD),
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == FakeRepoImplSymbols.CURRENCY_USD
					&& toCurrency == FakeRepoImplSymbols.CURRENCY_USD -> {
						Result.success(
							ResponseConversion(
								amountToConvert,
								null,
								null,
								null,
							)
						)
					}
				fromCurrency == FakeRepoImplSymbols.CURRENCY_USD
					&& toCurrency == FakeRepoImplSymbols.CURRENCY_EGP -> {
						Result.success(
							ResponseConversion(
								amountToConvert.div(FakeRepoImplSymbols.RATIO_CONVERSION_EGP_TO_USD),
								null,
								null,
								null,
							)
						)
					}
				else -> {
					throw RuntimeException("Not supported currencies in Fake repos")
				}
			}
		}

		override suspend fun getRatesOfCurrenciesForSpecificPeriod(
			startDate: LocalDate,
			endDate: LocalDate,
			baseCurrency: String,
			targetCurrencies: List<String>
		): Result<ResponseRatesOfCurrencies> {
			val rates = buildMap {
				repeat(
					Period.between(startDate, endDate.plusDays(1)).days
				) { day ->
					val localDate = startDate.plusDays(day.toLong())

					val key = localDate.convertToFormatYYYYMMDD()

					val value = buildMap {
						for (currency in targetCurrencies) {
							this += currency to FakeRepoImplSymbols.getRateOfConversion(
								baseCurrency, currency
							)
						}
					}

					this += key to value
				}
			}

			return Result.success(
				ResponseRatesOfCurrencies(
					startDate.convertToFormatYYYYMMDD(),
					endDate.convertToFormatYYYYMMDD(),
					baseCurrency,
					rates,
				)
			)
		}

	}

}
