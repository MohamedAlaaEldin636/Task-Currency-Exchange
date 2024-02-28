package my.ym.taskcurrencyexchange.ui.currencyDetails

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.junit.Test
import java.time.LocalDate
import java.time.Period

@RunWith(RobolectricTestRunner::class)
class TestABC {

	@Test
	fun fetchAllCurrencies() {
		val numOfDays = 3
		val todayAsEndDay = LocalDate.now()
		val startDate = todayAsEndDay.minusDays(numOfDays.dec().toLong())

		println(startDate)
		println(todayAsEndDay)
		println(
			Period.between(startDate, todayAsEndDay.plusDays(1)).days
		)

		println()
		repeat(
			Period.between(startDate, todayAsEndDay.plusDays(1)).days
		) { day ->
			println(day)
		}
	}

}
