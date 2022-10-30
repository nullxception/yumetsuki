package io.chaldeaprjkt.yumetsuki.data.store

import androidx.room.TypeConverter
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame

class YumeConverters {
    @TypeConverter
    fun fromHoYoGame(date: HoYoGame) = HoYoGame.Adapter.toJson(date)

    @TypeConverter
    fun toHoYoGame(value: Int) = HoYoGame.Adapter.fromJson(value)
}
