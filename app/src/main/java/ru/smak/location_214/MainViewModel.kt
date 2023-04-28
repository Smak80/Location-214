package ru.smak.location_214

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _location = mutableStateOf<Location>(Location(""))

    var location: Location
        get() = _location.value
        set(value) {
            _location.value = value
        }
}