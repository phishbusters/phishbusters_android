package com.phishbusters.clients.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.commandiron.compose_loading.Circle
import com.phishbusters.clients.R

@Composable
fun AppLoadingIndicator(
    custom: Boolean = true,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    if (!custom) {
        val context = LocalContext.current
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.spinner)
                    .size(coil.size.Size.ORIGINAL)
                    .build(),
                imageLoader = ImageLoader.Builder(context)
                    .components {
                        if (Build.VERSION.SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build(),
                modifier = modifier,
                contentDescription = "Cargando...",
            )
        }
    } else {
        Circle(
            Modifier.fillMaxSize()
        )
    }

}
