package com.huebeiro.reviewyourtour.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

@Entity
data class Review(
        @PrimaryKey(autoGenerate = true)
        var uid: Int = 0,
        @SerializedName("review_id")
        var reviewId: Int = 0,
        var rating: String,
        var title: String?,
        var message: String?,
        var author: String,
        var foreignLanguage: Boolean,
        var date: String,
        @SerializedName("date_unformatted")
        var dateUnformatted: Any,
        var languageCode: String,
        @SerializedName("traveler_type")
        var travelerType: String?,
        var reviewerName: String,
        var reviewerCountry: String
)