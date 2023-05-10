package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

enum class StarRailServer(override val regionId: String) : GameServer {
    ASIA("prod_official_asia"),
    EU("prod_official_euro"),
    TW("prod_official_cht"),
    USA("prod_official_usa");

    companion object {
        fun fromRegionId(value: String): StarRailServer {
            return values().associateBy(StarRailServer::regionId)[value] ?: ASIA
        }
    }
}
