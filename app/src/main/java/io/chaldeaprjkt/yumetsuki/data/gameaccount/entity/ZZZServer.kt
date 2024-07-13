package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

enum class ZZZServer(override val regionId: String) : GameServer {
    Asia("prod_gf_jp"),
    US("prod_gf_usa"),
    EU("prod_gf_eu"),
    CHT("prod_gf_sg");

    companion object {
        fun fromRegionId(value: String): ZZZServer {
            return values().associateBy(ZZZServer::regionId)[value] ?: US
        }
    }
}
