package my.ym.taskcurrencyexchange.data.remote.currenciesConversion

import my.ym.taskcurrencyexchange.models.ResponseRatesOfCurrencies
import java.time.LocalDate
import javax.inject.Inject

/**
 * - Follows conventions of the Android developers page themselves Check out this
 * [Link](https://developer.android.com/topic/architecture/domain-layer)
 *
 * - Side Note I know you know that link I'm just add nt the comment to tell you that I do as well
 * know it.
 */
class GetRatesOfCurrenciesForLastNDaysUseCase @Inject constructor(
	private val repoConversions: RepoConversions
) {

	/**
	 * @param numOfDays represents last N days ex. you want it for last 3 days
	 * (today, yesterday and the day before it) then add `3` to this param isa.
	 */
	suspend operator fun invoke(
		baseCurrency: String,
		targetCurrencies: List<String>,
		numOfDays: Int
	): Result<ResponseRatesOfCurrencies> {
		val todayAsEndDay = LocalDate.now()
		val startDate = todayAsEndDay.minusDays(numOfDays.dec().toLong())

		return repoConversions.getRatesOfCurrenciesForSpecificPeriod(
			startDate,
			todayAsEndDay,
			baseCurrency,
			targetCurrencies
		)
	}

}
