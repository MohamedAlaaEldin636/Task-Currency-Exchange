package my.ym.taskcurrencyexchange.models

data class TwoCurrenciesConversion(
	val baseCurrency: String,
	val baseValue: Double?,

	val targetCurrency: String,
	val targetValue: Double?,
)
