package my.ym.taskcurrencyexchange.extensions

/**
 * - In case you want to ensure that a number has a specified length, Ex. lets say you have "2"
 * and it represents a month of a length 2, then it will convert it to "02" if you input `2`
 * to [requiredLength] isa.
 */
fun String.minLengthZerosPrefix(requiredLength: Int): String {
	return if (length < requiredLength) {
		"${"0".repeat(requiredLength - length)}$this"
	}else {
		this
	}
}

/**
 * @see minLengthZerosPrefix
 */
fun Int.minLengthZerosPrefix(requiredLength: Int): String = toString().minLengthZerosPrefix(requiredLength)
