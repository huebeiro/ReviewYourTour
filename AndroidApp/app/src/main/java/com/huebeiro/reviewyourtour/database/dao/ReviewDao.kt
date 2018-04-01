package com.huebeiro.reviewyourtour.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.huebeiro.reviewyourtour.model.Review

/**
 * Author: Adilson Ribeiro
 * Date: 31/03/18
 */

@Dao
interface ReviewDao{
    @get:Query("SELECT * FROM review ORDER BY uid")
    val all: List<Review>

    @Insert
    fun insertAll(vararg reviews: Review)

    @Query("DELETE FROM review")
    fun deleteAll()


    @Query("SELECT * FROM review WHERE languageCode = :language ORDER BY uid")
    fun getReviewsByLanguage(language: String) : List<Review>
}