package com.oleksii.tomin.portfoliolayouts.ext

import android.view.View
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
fun View.scopedClickAndDebounce(debounceTime: Long = 300L) =
    clicks().debounce(debounceTime)
