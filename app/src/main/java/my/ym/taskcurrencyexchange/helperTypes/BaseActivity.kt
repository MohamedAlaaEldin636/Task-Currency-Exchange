package my.ym.taskcurrencyexchange.helperTypes

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

	protected lateinit var binding: VDB

	@LayoutRes
	abstract fun getLayoutRes(): Int

	open fun setBindingVariables() {}

	@CallSuper
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, getLayoutRes())
		setBindingVariables()
		binding.lifecycleOwner = this
	}

}
