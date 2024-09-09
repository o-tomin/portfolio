package com.oleksii.tomin.portfoliolayouts.profile

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    context: Context,
) : MviViewModel<ProfileViewModelState, ProfileViewModelEvents>(
    ProfileViewModelState(
        bluetoothState = BluetoothStatus.UNKNOWN
    )
) {

    private val _updateBluetoothStatus: () -> Unit

    init {
        _updateBluetoothStatus = {
            updateState {
                copy(
                    bluetoothState = fetchBluetoothStatus(context)
                )
            }
        }
    }

    fun updateBluetoothStatus() = _updateBluetoothStatus()

    private fun fetchBluetoothStatus(context: Context): BluetoothStatus {
        // Check if the device supports Bluetooth
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
            ?: // Device does not support Bluetooth
            return BluetoothStatus.NOT_SUPPORTED

        // Check if the device supports Bluetooth LE
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // Device does not support BLE
            return BluetoothStatus.BLE_NOT_SUPPORTED
        }

        // Check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled
            return BluetoothStatus.NOT_ENABLED
        }

        val hasBluetoothConnectPermission =
            context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (!hasBluetoothConnectPermission) {
            // Permission not granted
            return BluetoothStatus.PERMISSIONS_NOT_GRANTED
        }

        // All checks passed, BLE is supported and ready to use
        return BluetoothStatus.OK
    }

}

data class ProfileViewModelState(
    val bluetoothState: BluetoothStatus,
) : MviViewState

sealed class ProfileViewModelEvents {
    data class Error(val t: Throwable)
}