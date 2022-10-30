package io.chaldeaprjkt.yumetsuki.data.dataswitch.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.dataswitch.DataSwitchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSwitchNetworkSourceImpl @Inject constructor(
    private val dataSwitchApi: DataSwitchApi
) : DataSwitchNetworkSource {
    override suspend fun get(
        gameId: Int,
        switchId: Int,
        isPublic: Boolean,
        cookie: String,
        ds: String,
    ) = flow<HoYoResult<HoYoEmptyData>> {
        dataSwitchApi.changeDataSwitch(gameId, switchId, isPublic, cookie, ds)
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(it.retcode, it.message, it.data ?: HoYoResult.Empty)
                } ?: HoYoResult.Null())
            }.suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }.suspendOnException {
                exception.printStackTrace()
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)
}
