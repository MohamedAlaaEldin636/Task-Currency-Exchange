package my.ym.taskcurrencyexchange.extensions

import java.math.RoundingMode

fun Int?.orZero() = this ?: 0

fun Float?.orZero() = this ?: 0f
fun Long?.orZero() = this ?: 0L
fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false

fun Double.toIntIfNoFractionsOrThis(): Number = if (this - toInt().toDouble() == 0.0) toInt() else this

fun Double.round(decimalsCount: Int): Double =
	toBigDecimal().setScale(decimalsCount, RoundingMode.HALF_UP).toDouble()
