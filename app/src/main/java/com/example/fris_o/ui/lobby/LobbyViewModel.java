package com.example.fris_o.ui.lobby;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LobbyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LobbyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is lobby fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}