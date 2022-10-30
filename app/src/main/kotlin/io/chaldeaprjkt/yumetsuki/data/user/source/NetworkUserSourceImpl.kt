package io.chaldeaprjkt.yumetsuki.data.user.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.user.UserFullInfoApi
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserFullInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUserSourceImpl @Inject constructor(
    private val userInfoApi: UserFullInfoApi
) : NetworkUserSource {
    override suspend fun fetch(cookie: String) = flow<HoYoResult<UserFullInfo>> {
        userInfoApi.fetch(HoYoCookie(cookie).lang, cookie)
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(it.retcode, it.message, it.data ?: UserFullInfo.Empty)
                } ?: HoYoResult.Null())
            }
            .suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }
            .suspendOnException {
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)
}
