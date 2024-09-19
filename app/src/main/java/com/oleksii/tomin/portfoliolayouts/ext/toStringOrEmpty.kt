package com.oleksii.tomin.portfoliolayouts.ext

import android.text.Editable

fun Editable?.toStringOrEmpty() = this?.toString() ?: ""
