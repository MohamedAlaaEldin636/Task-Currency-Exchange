package my.ym.taskcurrencyexchange.extensions

import java.time.LocalDate

object LocalDateTimeUtils {

	fun convertFromFormatYYYYMMDD(value: String?, separator: String = "-"): LocalDate? {
		val (year, month, day) = value.orEmpty().split(separator).let {
			Triple(
				it.getOrNull(0)?.toIntOrNull() ?: return null,
				it.getOrNull(1)?.toIntOrNull() ?: return null,
				it.getOrNull(2)?.toIntOrNull() ?: return null,
			)
		}

		return LocalDate.of(year, month, day)
	}

}

fun LocalDate.convertToFormatYYYYMMDD(separator: String = "-"): String {
	return buildString {
		append(
			year.minLengthZerosPrefix(4)
		)
		append(separator)
		append(
			monthValue.minLengthZerosPrefix(2)
		)
		append(separator)
		append(
			dayOfMonth.minLengthZerosPrefix(2)
		)
	}
}
