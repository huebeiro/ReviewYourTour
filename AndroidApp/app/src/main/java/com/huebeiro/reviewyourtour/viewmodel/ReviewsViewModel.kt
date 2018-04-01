package com.huebeiro.reviewyourtour.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableField

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

class ReviewsViewModel : BaseViewModel(){

    val reviews = ObservableArrayList<ReviewViewModel>()
    val loading = ObservableField<Boolean>()

}