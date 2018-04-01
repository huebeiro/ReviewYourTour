package com.huebeiro.reviewyourtour.viewmodel

import android.databinding.BaseObservable
import android.text.Html
import android.text.Spanned
import android.view.View
import com.huebeiro.reviewyourtour.model.Review

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

class ReviewViewModel(var review: Review) : BaseObservable(){

    fun getFloatRating() : Float{
        return review.rating.toFloat()
    }

    fun getTitleVisibility() : Int{
        return if (review.title == null || review.title?.length == 0)
            View.GONE
        else
            View.VISIBLE
    }

    fun getMessageVisibility() : Int{
        return if (review.message == null || review.message?.length == 0)
            View.GONE
        else
            View.VISIBLE
    }

    fun getFormattedMessage() : Spanned?{
        return if (review.message == null)
            null
        else
            Html.fromHtml(review.message)
    }
}