package my.ym.taskcurrencyexchange.extensions

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <VDB : ViewDataBinding> View.findBindingOrNull(): VDB? {
	return DataBindingUtil.findBinding(this)
}
