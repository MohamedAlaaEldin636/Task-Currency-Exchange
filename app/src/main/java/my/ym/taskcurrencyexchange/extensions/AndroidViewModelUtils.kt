package my.ym.taskcurrencyexchange.extensions

import androidx.lifecycle.AndroidViewModel
import my.ym.taskcurrencyexchange.MyApp

val AndroidViewModel.myApp: MyApp
	get() = getApplication()
