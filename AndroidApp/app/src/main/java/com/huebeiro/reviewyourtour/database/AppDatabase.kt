package com.huebeiro.reviewyourtour.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.huebeiro.reviewyourtour.database.dao.ReviewDao
import com.huebeiro.reviewyourtour.model.Review

/**
 * Author: Adilson Ribeiro
 * Date: 31/03/18
 */
@Database(
        entities = [
            Review::class
        ],
        version = 1
)
@TypeConverters(
        Converters::class
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun reviewDao() : ReviewDao
}