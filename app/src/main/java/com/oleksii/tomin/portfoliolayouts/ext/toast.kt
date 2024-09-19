package com.oleksii.tomin.portfoliolayouts.ext

import android.widget.Toast
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment

fun MviFragment.toast(message: String) =
    Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()