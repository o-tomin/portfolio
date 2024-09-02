package com.oleksii.tomin.portfoliolayouts.ext

import android.view.MenuItem
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun NavigationBarView.selects(): Flow<MenuItem> {
    return callbackFlow {
        setOnItemSelectedListener {
            trySend(it)
            true
        }
        awaitClose { setOnClickListener(null) }
    }
}