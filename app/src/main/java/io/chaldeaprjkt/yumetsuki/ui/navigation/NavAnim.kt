package io.chaldeaprjkt.yumetsuki.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset

fun <S> AnimatedContentTransitionScope<S>.fadeSlideInDown(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { -100 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Down,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentTransitionScope<S>.fadeSlideInRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { it / 2 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Right,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentTransitionScope<S>.fadeSlideInLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    initialOffset: (Int) -> Int = { it / 2 }
) = fadeIn() + slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Left,
    animationSpec = animationSpec,
    initialOffset = initialOffset
)

fun <S> AnimatedContentTransitionScope<S>.fadeSlideOutLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    targetOffset: (Int) -> Int = { it / 2 }
) = fadeOut() + slideOutOfContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Left,
    animationSpec = animationSpec,
    targetOffset = targetOffset
)

fun <S> AnimatedContentTransitionScope<S>.fadeSlideOutRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(250),
    targetOffset: (Int) -> Int = { it / 2 }
) = fadeOut() + slideOutOfContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Right,
    animationSpec = animationSpec,
    targetOffset = targetOffset
)
