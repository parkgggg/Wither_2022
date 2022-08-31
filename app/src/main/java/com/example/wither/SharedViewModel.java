package com.example.wither;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<MakeDatabase> liveData = new MutableLiveData<>();
    public LiveData<MakeDatabase> getLiveData() {
        return liveData;
    }
    public void setLiveData(MakeDatabase database) {
        liveData.setValue(database);
    }
}
