package io.chaldeaprjkt.yumetsuki.data.user.source

import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
import io.chaldeaprjkt.yumetsuki.data.user.UserFullInfoApi
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserFullInfo
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

@Singleton
class UserNetworkSourceImpl @Inject constructor(private val userInfoApi: UserFullInfoApi) :
    UserNetworkSource {
    override suspend fun fetch(cookie: String) =
        userInfoApi
            .fetch(HoYoCookie(cookie).lang, cookie, CommonFunction.genDS())
            .flowAsResult(UserFullInfo.Empty)
            .flowOn(Dispatchers.IO)
}
