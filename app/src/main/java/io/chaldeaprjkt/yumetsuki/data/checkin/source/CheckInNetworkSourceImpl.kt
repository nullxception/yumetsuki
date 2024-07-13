package io.chaldeaprjkt.yumetsuki.data.checkin.source

import io.chaldeaprjkt.yumetsuki.data.checkin.CheckInApi
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInNetworkSourceImpl @Inject constructor(private val checkInApi: CheckInApi) :
    CheckInNetworkSource {

    override suspend fun status(
        cookie: String,
        game: HoYoGame,
    ) = checkInApi.run {
        val lang = HoYoCookie(cookie).lang
        when (game) {
            HoYoGame.ZZZ -> zzzStatus(lang, ZZZActID, cookie)
            HoYoGame.StarRail -> starRailStatus(lang, StarRailActID, cookie)
            HoYoGame.Houkai -> houkaiStatus(lang, HoukaiActID, cookie)
            else -> genshinStatus(lang, GenshinActID, cookie)
        }
    }.flowAsResult(CheckInNoteResult.Empty).flowOn(Dispatchers.IO)

    override suspend fun checkIn(
        cookie: String, game: HoYoGame
    ) = checkInApi.run {
        val lang = HoYoCookie(cookie).lang

        when (game) {
            HoYoGame.ZZZ -> zzz(lang, ZZZActID, cookie)
            HoYoGame.StarRail -> starRail(lang, StarRailActID, cookie)
            HoYoGame.Houkai -> houkai(lang, HoukaiActID, cookie)
            else -> genshin(lang, GenshinActID, cookie)
        }
    }.flowAsResult(CheckInResult.Empty).flowOn(Dispatchers.IO)

    companion object {
        const val HoukaiActID = "e202110291205111"
        const val GenshinActID = "e202102251931481"
        const val StarRailActID = "e202303301540311"
        const val ZZZActID = "e202406031448091"
    }
}
