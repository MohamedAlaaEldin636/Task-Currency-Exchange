package my.ym.taskcurrencyexchange.di.modules

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import my.ym.taskcurrencyexchange.extensions.asBundle
import my.ym.taskcurrencyexchange.ui.currencyDetails.CurrencyDetailsFragmentArgs

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

	@Provides
	fun provideCurrencyDetailsFragmentArgs(state: SavedStateHandle): CurrencyDetailsFragmentArgs {
		return CurrencyDetailsFragmentArgs.fromBundle(state.asBundle())
	}

}
