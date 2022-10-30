package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

enum class HoukaiServer(override val regionId: String) : GameServer {
    CN("cn01"),
    KR("kr01"),
    JP("jp01"),
    TW("asia01"),
    SEA("overseas01"),
    EU("eur01"),
    Global("usa01");

    companion object {
        fun fromRegionId(value: String): HoukaiServer {
            return values().associateBy(HoukaiServer::regionId)[value] ?: Global
        }
    }
}
