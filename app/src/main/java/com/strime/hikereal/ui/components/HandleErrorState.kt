package com.strime.hikereal.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.strime.hikereal.utils.UiState


@Composable
fun <T> HandleErrorState(errorState: UiState<T>, snackbarHostState: SnackbarHostState) {
    if (errorState is UiState.Error) {

        val errorMessage = stringResource(errorState.errorCode)

        LaunchedEffect(errorState) {
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
