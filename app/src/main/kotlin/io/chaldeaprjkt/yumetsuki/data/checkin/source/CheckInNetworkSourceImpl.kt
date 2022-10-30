package io.chaldeaprjkt.yumetsuki.data.checkin.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.data.checkin.CheckInApi
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckIn
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInNetworkSourceImpl @Inject constructor(
    private val checkInApi: CheckInApi
) : CheckInNetworkSource {
    override suspend fun genshin(
        lang: String,
        actId: String,
        cookie: String,
    ) = flow<HoYoResult<CheckIn>> {
        checkInApi.genshin(lang, actId, cookie)
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(it.retcode, it.message, it.data ?: CheckIn.Empty)
                } ?: HoYoResult.Null())
            }.suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }.suspendOnException {
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun houkai(
        lang: String,
        actId: String,
        cookie: String,
    ) = flow<HoYoResult<CheckIn>> {
        checkInApi.houkai(lang, actId, cookie)
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(it.retcode, it.message, it.data ?: CheckIn())
                } ?: HoYoResult.Null())
            }
            .suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }
            .suspendOnException {
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)
}
