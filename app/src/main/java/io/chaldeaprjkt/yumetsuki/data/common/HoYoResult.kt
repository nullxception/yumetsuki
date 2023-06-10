@file:Suppress("unused")

package io.chaldeaprjkt.yumetsuki.data.common

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.flow

sealed interface HoYoResult<T>

data class HoYoData<T>(val code: HoYoApiCode, val message: String, val data: T) : HoYoResult<T>

sealed interface HoYoError<T> : HoYoResult<T> {
    data class Api<T>(val code: HoYoApiCode, val message: String = "") : HoYoError<T>
    data class Network<T>(val code: Int, val message: String = "") : HoYoError<T>
    data class Code<T>(val e: Throwable) : HoYoError<T>
    class Empty<T> : HoYoError<T>
}

suspend inline fun <T> HoYoResponse<T>.flowAsResult(empty: T) =
    flow<HoYoResult<T>> {
        suspendOnSuccess {
                val body = response.body()
                if (body == null) {
                    emit(HoYoError.Empty())
                    return@suspendOnSuccess
                }

                when (body.retcode) {
                    HoYoApiCode.Success -> {
                        emit(HoYoData(body.retcode, body.message, body.data ?: empty))
                    }
                    else -> {
                        emit(HoYoError.Api(body.retcode, body.message))
                    }
                }
            }
            .suspendOnError { emit(HoYoError.Network(response.code(), response.message())) }
            .suspendOnException { emit(HoYoError.Code(exception)) }
    }
