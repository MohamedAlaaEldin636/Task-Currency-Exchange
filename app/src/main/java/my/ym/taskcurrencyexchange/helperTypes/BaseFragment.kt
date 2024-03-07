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
import androidx.lifecycle.distinctUntilChanged
import kotlinx.coroutines.cancel
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.extensions.dismissGlobalErrorHandlingDialog
import my.ym.taskcurrencyexchange.extensions.dismissGlobalLoadingDialog
import my.ym.taskcurrencyexchange.extensions.goBackIfPossible
import my.ym.taskcurrencyexchange.extensions.showGlobalErrorHandlingDialog
import my.ym.taskcurrencyexchange.extensions.showGlobalLoadingDialog
import my.ym.taskcurrencyexchange.extensions.toast

abstract class BaseFragment<VDB : ViewDataBinding, VM : BaseAndroidViewModel> : Fragment() {

	protected var binding: VDB? = null
		private set

	protected abstract val viewModel: VM

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
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		observeGlobalLoading()

		observeGlobalError()

		setupViews()

		observeLiveData()
	}

	@CallSuper
	override fun onResume() {
		super.onResume()

		showOrHideGlobalLoading(viewModel.showGlobalLoadingDialog.value)

		showOrHideGlobalError(viewModel.showGlobalErrorHandlingDialog.value)
	}

	protected open fun observeGlobalLoading() {
		viewModel.showGlobalLoadingDialog.distinctUntilChanged().observe(viewLifecycleOwner) {
			if (isResumed) {
				showOrHideGlobalLoading(it)
			}
		}
	}

	protected open fun observeGlobalError() {
		viewModel.showGlobalErrorHandlingDialog.distinctUntilChanged().observe(viewLifecycleOwner) {
			if (isResumed) {
				showOrHideGlobalError(it)
			}
		}
	}

	protected open fun showOrHideGlobalLoading(it: BaseAndroidViewModel.GlobalLoadingData?) {
		if (it != null) {
			showGlobalLoadingDialog {
				viewModel.showGlobalLoadingDialog.value = null

				it.coroutineScope.cancel()

				context?.toast(getString(R.string.you_cancelled_the_action_successfully))

				dismissGlobalLoadingDialog()

				when (it.fallbackAction) {
					BaseAndroidViewModel.FallbackAction.CANCEL -> {}
					BaseAndroidViewModel.FallbackAction.GO_BACK -> {
						goBackIfPossible()
					}
				}
			}
		}else {
			dismissGlobalLoadingDialog()
		}
	}

	protected open fun showOrHideGlobalError(it: BaseAndroidViewModel.GlobalErrorData?) {
		if (it != null) {
			val negativeButtonText = when (it.fallbackAction) {
				BaseAndroidViewModel.FallbackAction.CANCEL -> getString(R.string.cancel)
				BaseAndroidViewModel.FallbackAction.GO_BACK -> getString(R.string.back)
			}

			showGlobalErrorHandlingDialog(
				it.msg,
				negativeButtonText,
				negativeButtonAction = {
					viewModel.showGlobalErrorHandlingDialog.value = null

					dismissGlobalErrorHandlingDialog()

					when (it.fallbackAction) {
						BaseAndroidViewModel.FallbackAction.CANCEL -> {}
						BaseAndroidViewModel.FallbackAction.GO_BACK -> {
							goBackIfPossible()
						}
					}
				}
			) {
				viewModel.showGlobalErrorHandlingDialog.value = null

				dismissGlobalErrorHandlingDialog()

				it.retryAbleFun()
			}
		}else {
			dismissGlobalErrorHandlingDialog()
		}
	}

	protected open fun setupViews() {}

	protected open fun observeLiveData() {}

	@CallSuper
	override fun onDestroyView() {
		binding = null

		super.onDestroyView()
	}

}
