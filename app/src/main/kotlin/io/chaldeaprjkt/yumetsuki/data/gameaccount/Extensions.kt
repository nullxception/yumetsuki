package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GenshinServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoukaiServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount

val GameAccount.server: GameServer
    get() = when (game) {
        HoYoGame.Houkai -> HoukaiServer.fromRegionId(region)
        HoYoGame.Genshin -> GenshinServer.fromRegionId(region)
        else -> throw NoSuchElementException("No identifiable server")
    }

fun GameAccount.isEmpty() =
    uid == 0 && region.isEmpty()
