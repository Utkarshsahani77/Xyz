package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UtkarshPink
import com.example.ui.theme.UtkarshRed
import com.example.util.PlayStationSoundPlayer
import com.example.util.rememberHapticFeedback
import kotlinx.coroutines.delay

@Composable
fun AppleMusicSplashScreen(
    onAnimationFinished: () -> Unit
) {
    val haptics = rememberHapticFeedback()
    val logoScale = remember { Animatable(0.7f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val glowScale = remember { Animatable(0.6f) }
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Trigger authentic Sony PlayStation 5 Startup Chime & Spatial Bloom
        PlayStationSoundPlayer.playPS5StartupSound()
        haptics.playPauseClick()

        // Step 1: Smooth fluid expansion of the iconic music note logo synced with PS5 chime
        logoAlpha.animateTo(1f, animationSpec = tween(450, easing = FastOutSlowInEasing))
        glowScale.animateTo(1.35f, animationSpec = tween(850, easing = FastOutSlowInEasing))
        logoScale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
                dampingRatio = 0.65f,
                stiffness = 260f
            )
        )

        // Step 2: Fade in Utkarsh Music typography during the warm harmonic pad bloom
        textAlpha.animateTo(1f, animationSpec = tween(400))
        haptics.toggleMode()
        delay(650)

        // Step 3: Gentle scale bloom & tactile feedback as the PS5 shimmer decays
        logoScale.animateTo(1.06f, animationSpec = tween(300, easing = FastOutSlowInEasing))
        delay(300)

        isVisible = false
        delay(250)
        onAnimationFinished()
    }

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("apple_music_splash_screen"),
            contentAlignment = Alignment.Center
        ) {
            // Ambient glowing halo behind logo
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(glowScale.value)
                    .blur(60.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                UtkarshRed.copy(alpha = 0.6f),
                                UtkarshPink.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Iconic Apple Music note logo tile
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(logoScale.value)
                        .shadow(24.dp, RoundedCornerShape(26.dp), spotColor = UtkarshRed)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(UtkarshPink, UtkarshRed, Color(0xFFC41230))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♫",
                        fontSize = 58.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Brand Wordmark
                Text(
                    text = "Utkarsh Music",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color.White.copy(alpha = textAlpha.value)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "LOSSLESS • SPATIAL AUDIO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = UtkarshRed
                    ),
                    color = UtkarshRed.copy(alpha = textAlpha.value)
                )
            }
        }
    }
}
