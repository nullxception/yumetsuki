package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GenshinServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoukaiServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.StarRailServer

val GameAccount.server: GameServer
    get() =
        when (game) {
            HoYoGame.Houkai -> HoukaiServer.fromRegionId(region)
            HoYoGame.Genshin -> GenshinServer.fromRegionId(region)
            HoYoGame.StarRail -> StarRailServer.fromRegionId(region)
            else -> throw NoSuchElementException("No identifiable server")
        }

fun GameAccount.isEmpty() = uid == 0 && region.isEmpty()
