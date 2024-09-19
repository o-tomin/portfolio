package com.oleksii.tomin.portfoliolayouts.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment

fun MviFragment.copyTextToClipboard(text: String) {
    (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(ClipData.newPlainText(text, text))
}