package io.chaldeaprjkt.yumetsuki.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset

fun <S> AnimatedContentScope<S>.fadeSlideInDown(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { -100 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentScope.SlideDirection.Down,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentScope<S>.fadeSlideInRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { it / 2 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentScope.SlideDirection.Right,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentScope<S>.fadeSlideInLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { it / 2 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentScope.SlideDirection.Left,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentScope<S>.fadeSlideOutLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    targetOffset: (Int) -> Int = { it / 2 }
) = fadeOut() + slideOutOfContainer(
    towards = AnimatedContentScope.SlideDirection.Left,
    animationSpec = animationSpec,
    targetOffset = targetOffset
)

fun <S> AnimatedContentScope<S>.fadeSlideOutRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    targetOffset: (Int) -> Int = { it / 2 }
) = fadeOut() + slideOutOfContainer(
    towards = AnimatedContentScope.SlideDirection.Right,
    animationSpec = animationSpec,
    targetOffset = targetOffset
)
