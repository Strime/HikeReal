/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strime.hikereal.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Dimensions partagées pour toute l'application
 */
object Dimens {
    // Padding et spacing
    val paddingSmall = 4.dp
    val paddingMedium = 8.dp
    val paddingLarge = 12.dp
    val paddingExtraLarge = 16.dp

    val spacingTiny = 2.dp
    val spacingSmall = 4.dp
    val spacingMedium = 8.dp
    val spacingLarge = 16.dp
    val spacingExtraLarge = 24.dp

    // Sizing
    val iconSizeSmall = 14.dp
    val iconSizeMedium = 16.dp
    val iconSizeLarge = 24.dp
    val iconSizeExtraLarge = 40.dp

    val cardElevation = 1.dp
    val borderWidth = 1.dp

    val borderRadiusSmall = 4.dp
    val borderRadiusMedium = 8.dp
    val borderRadiusLarge = 12.dp
    val borderRadiusRound = 50.dp

    val profilePictureSize = 40.dp
    val pipImageSize = 120.dp
    val photoIndicatorSize = 8.dp

    // Opacité
    const val surfaceOpacity = 0.9f
    const val indicatorOpacity = 0.5f
    const val overlayOpacity = 0.5f
    const val secondaryTextOpacity = 0.8f
    const val primaryIconOpacity = 0.9f
    const val secondaryIconOpacity = 0.8f

    // Typographie (si non définie dans Typography)
    val fontSizeExtraSmall = 10.sp
    val fontSizeSmall = 12.sp
    val fontSizeMedium = 14.sp
    val fontSizeLarge = 16.sp
}