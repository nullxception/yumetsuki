package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

enum class GenshinServer(override val regionId: String) : GameServer {
    Asia("os_asia"),
    US("os_usa"),
    EU("os_euro"),
    CHT("os_cht");

    companion object {
        fun fromRegionId(value: String): GenshinServer {
            return values().associateBy(GenshinServer::regionId)[value] ?: US
        }
    }
}
