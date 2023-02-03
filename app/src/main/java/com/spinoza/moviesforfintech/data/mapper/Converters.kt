package com.spinoza.moviesforfintech.data.mapper

import androidx.room.TypeConverter
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.DELIMITER
import java.util.stream.Collectors

class Converters {
    @TypeConverter
    fun fromList(list: List<String?>): String {
        return list.stream().collect(Collectors.joining(DELIMITER))
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return data.split(DELIMITER).toList()
    }
}