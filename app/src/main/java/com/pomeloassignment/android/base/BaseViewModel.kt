package com.pomeloassignment.android.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/*
* Base class for Viewmodel
* Works based on state change. And the view listening to the state changes behavior
*/
open class BaseViewModel<S : Any>(initialState: S) : ViewModel() {
    private val _state: MutableLiveData<S> = MutableLiveData(initialState)
    val state: LiveData<S> get() = _state

    //interact with view based on state from VM
    @MainThread
    protected fun setState(reducer: S.() -> S) {
        val currentState = _state.value!!
        val newState = currentState.reducer()
        if (newState != currentState) {
            _state.value = newState
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = f() as T
    }