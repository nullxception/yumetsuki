package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

enum class StarRailServer(override val regionId: String) : GameServer {
    ASIA("prod_official_asia");

    companion object {
        fun fromRegionId(value: String): StarRailServer {
            return values().associateBy(StarRailServer::regionId)[value] ?: ASIA
        }
    }
}
