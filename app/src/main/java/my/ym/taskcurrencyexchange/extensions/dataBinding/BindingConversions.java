package my.ym.taskcurrencyexchange.extensions.dataBinding;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingConversion;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@SuppressWarnings("unused")
public class BindingConversions {

    @BindingConversion
    public static int convertBooleanToViewVisibility(@Nullable Boolean isVisible) {
        if (isVisible != null && isVisible) {
            return View.VISIBLE;
        }else {
            return View.GONE;
        }
    }

    @BindingConversion
    public static int convertBooleanToViewVisibility(@Nullable LiveData<Boolean> value) {
        Boolean bool = null;
        if (value != null) {
            bool = value.getValue();
        }
        return convertBooleanToViewVisibility(bool);
    }

    @BindingConversion
    public static int convertBooleanToViewVisibility(@Nullable MutableLiveData<Boolean> value) {
        Boolean bool = null;
        if (value != null) {
            bool = value.getValue();
        }
        return convertBooleanToViewVisibility(bool);
    }

}
