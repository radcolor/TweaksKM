package com.theradcolor.kernel.ui.Tweaks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TweaksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TweaksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}