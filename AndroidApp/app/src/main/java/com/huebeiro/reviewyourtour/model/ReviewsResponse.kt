package com.huebeiro.reviewyourtour.model

import com.google.gson.annotations.SerializedName

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

data class ReviewsResponse(
    var status: Boolean,
    @SerializedName("total_reviews_comments")
    var totalComments: Int,
    var data: ArrayList<Review>
)