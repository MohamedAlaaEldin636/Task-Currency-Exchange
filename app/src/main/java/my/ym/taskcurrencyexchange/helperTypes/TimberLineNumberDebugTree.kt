package my.ym.taskcurrencyexchange.helperTypes

import timber.log.Timber

class TimberLineNumberDebugTree : Timber.DebugTree() {
	override fun createStackElementTag(element: StackTraceElement): String {
		return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
	}
}
