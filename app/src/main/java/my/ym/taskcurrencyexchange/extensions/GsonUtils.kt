package my.ym.taskcurrencyexchange.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object GsonUtils {
	val gson: Gson by lazy {
		GsonBuilder()
			.disableHtmlEscaping()
			.setLenient()
			.serializeNulls()
			.create()
	}
}

inline fun <reified T> T?.toJsonOrNull(): String? = kotlin.runCatching {
	GsonUtils.gson.toJson(this, object : TypeToken<T>() {}.type)
}.getOrNull()

inline fun <reified T> String?.fromJsonOrNull(): T? = kotlin.runCatching {
	GsonUtils.gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
}.getOrNull()
