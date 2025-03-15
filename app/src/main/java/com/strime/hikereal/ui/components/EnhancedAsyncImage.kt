package com.strime.hikereal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.strime.hikereal.R
import com.strime.hikereal.ui.theme.Dimens

@Composable
fun EnhancedAsyncImage(
    model: Any,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    val imageRequest = remember(retryTrigger, model) {
        ImageRequest.Builder(context)
            .data(model)
            .memoryCacheKey(if (model is String) model else null)
            .diskCacheKey(if (model is String) model else null)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .size(1080)
            .crossfade(300)
            .build()
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                isLoading = true
                isError = false
            },
            onError = {
                isLoading = false
                isError = true
            },
            onSuccess = {
                isLoading = false
                isError = false
                onSuccess?.invoke(it)
            }
        )

        if (isLoading) {
            if (placeholder != null) {
                placeholder()
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimens.iconSizeMedium),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        retryTrigger++
                        isLoading = true
                        isError = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                    Text(
                        text = stringResource(R.string.image_load_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                    Text(
                        text = stringResource(R.string.retry),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(Dimens.borderRadiusSmall)
                            )
                            .padding(
                                horizontal = Dimens.paddingMedium,
                                vertical = Dimens.paddingSmall
                            )
                    )
                }
            }
        }
    }
}