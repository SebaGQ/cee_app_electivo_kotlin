package com.kotlin.cee_app.ui.results.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultsViewModel : ViewModel() {
    private val _text = MutableLiveData("Resultados")
    val text: LiveData<String> = _text
}
