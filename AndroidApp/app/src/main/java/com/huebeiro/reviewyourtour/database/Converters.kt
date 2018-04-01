package com.huebeiro.reviewyourtour.database

import android.arch.persistence.room.TypeConverter

/**
 * Author: Adilson Ribeiro
 * Date: 31/03/18
 */
class Converters{

    @TypeConverter
    fun anyToString(value: Any?) : String?{
        return if (value == null) null else value.toString()
    }

    @TypeConverter
    fun anyFromString(value: String?) : Any?{
        return if (value == null) null else value
    }
}