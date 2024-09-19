package com.oleksii.tomin.portfoliolayouts.ext

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.clicks(): Flow<View> {
    return callbackFlow {
        setOnClickListener { trySend(it) }
        awaitClose { setOnClickListener(null) }
    }
}