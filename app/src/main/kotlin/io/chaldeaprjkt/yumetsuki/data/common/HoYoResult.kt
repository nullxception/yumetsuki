package io.chaldeaprjkt.yumetsuki.data.common

@Suppress("unused")
sealed class HoYoResult<T> {
    class Success<T>(val code: HoYoApiCode, val message: String, val data: T) : HoYoResult<T>()

    class Failure<T>(val httpCode: Int, val message: String) : HoYoResult<T>()

    class Error<T>(val e: Throwable) : HoYoResult<T>()

    class Null<T> : HoYoResult<T>()

    companion object {
        val Empty = HoYoEmptyData()
    }
}
