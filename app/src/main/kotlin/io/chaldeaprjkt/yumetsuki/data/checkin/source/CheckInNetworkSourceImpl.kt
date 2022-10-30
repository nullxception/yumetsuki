package io.chaldeaprjkt.yumetsuki.data.checkin.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.data.checkin.CheckInApi
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInStatus
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInNetworkSourceImpl @Inject constructor(
    private val checkInApi: CheckInApi
) : CheckInNetworkSource {

    override suspend fun status(
        cookie: String,
        game: HoYoGame,
    ) = flow<HoYoResult<CheckInStatus>> {
        val lang = HoYoCookie(cookie).lang

        checkInApi.run {
            if (game == HoYoGame.Houkai) {
                houkaiStatus(lang, HoukaiActID, cookie)
            } else {
                genshinStatus(lang, GenshinActID, cookie)
            }
        }.suspendOnSuccess {
            emit(response.body()?.let {
                HoYoResult.Success(it.retcode, it.message, it.data ?: CheckInStatus.Empty)
            } ?: HoYoResult.Null())
        }.suspendOnError {
            emit(HoYoResult.Failure(response.code(), response.message()))
        }.suspendOnException {
            emit(HoYoResult.Error(exception))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun checkIn(
        cookie: String,
        game: HoYoGame
    ) = flow<HoYoResult<CheckInResult>> {
        val lang = HoYoCookie(cookie).lang

        checkInApi.run {
            if (game == HoYoGame.Houkai) {
                houkai(lang, HoukaiActID, cookie)
            } else {
                genshin(lang, GenshinActID, cookie)
            }
        }.suspendOnSuccess {
            emit(response.body()?.let {
                HoYoResult.Success(it.retcode, it.message, it.data ?: CheckInResult.Empty)
            } ?: HoYoResult.Null())
        }.suspendOnError {
            emit(HoYoResult.Failure(response.code(), response.message()))
        }.suspendOnException {
            emit(HoYoResult.Error(exception))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val HoukaiActID = "e202110291205111"
        const val GenshinActID = "e202102251931481"
    }
}
