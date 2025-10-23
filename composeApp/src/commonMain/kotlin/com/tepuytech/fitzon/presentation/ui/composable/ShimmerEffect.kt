package com.tepuytech.fitzon.presentation.ui.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        Color(0xFF1B4332).copy(alpha = 0.3f),
        Color(0xFF2D6A4F).copy(alpha = 0.5f),
        Color(0xFF1B4332).copy(alpha = 0.3f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 1000f, translateAnim.value - 1000f),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    Box(
        modifier = modifier.background(brush)
    )
}

@Composable
fun AthleteDashboardShimmer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Header shimmer
            ShimmerEffect(
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ShimmerEffect(
                modifier = Modifier
                    .width(250.dp)
                    .height(24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // WOD Card shimmer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF2D6A4F).copy(alpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(28.dp)
                            )
                        }

                        ShimmerEffect(
                            modifier = Modifier
                                .size(56.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ShimmerEffect(
                        modifier = Modifier
                            .width(220.dp)
                            .height(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats shimmer
            ShimmerEffect(
                modifier = Modifier
                    .width(140.dp)
                    .height(24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBackground
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(120.dp)
                                .height(20.dp)
                        )
                        ShimmerEffect(
                            modifier = Modifier
                                .width(50.dp)
                                .height(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(2) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ShimmerEffect(
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ShimmerEffect(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ShimmerEffect(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Records shimmer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .width(180.dp)
                        .height(24.dp)
                )
                ShimmerEffect(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            repeat(3) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = cardBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(14.dp)
                            )
                        }

                        ShimmerEffect(
                            modifier = Modifier
                                .width(60.dp)
                                .height(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Leaderboard shimmer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .width(180.dp)
                        .height(24.dp)
                )
                ShimmerEffect(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBackground
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    repeat(5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ShimmerEffect(
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                ShimmerEffect(
                                    modifier = Modifier
                                        .size(40.dp),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                ShimmerEffect(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(18.dp)
                                )
                            }

                            ShimmerEffect(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(18.dp)
                            )
                        }

                        if (it < 4) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
