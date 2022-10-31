package io.chaldeaprjkt.yumetsuki.domain.common

import androidx.annotation.StringRes
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode

sealed interface UseCaseResult {
    data class Loading(@StringRes val messageId: Int) : UseCaseResult
    data class Success(
        @StringRes val messageId: Int,
        val args: List<Any> = emptyList(),
    ) : UseCaseResult

    data class Error(
        @StringRes val messageId: Int,
        val errCode: HoYoApiCode = HoYoApiCode.Unknown,
        val exception: Throwable? = null,
    ) : UseCaseResult
}
