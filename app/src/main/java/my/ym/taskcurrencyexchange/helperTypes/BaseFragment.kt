package my.ym.taskcurrencyexchange.helperTypes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

	protected var binding: VDB? = null
		private set

	@LayoutRes
	abstract fun getLayoutRes(): Int

	open fun setBindingVariables() {}

	@CallSuper
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

		setBindingVariables()
		binding?.lifecycleOwner = viewLifecycleOwner

		return binding?.root
	}

	@CallSuper
	override fun onDestroyView() {
		binding = null

		super.onDestroyView()
	}

}
