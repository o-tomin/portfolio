package com.oleksii.tomin.portfoliolayouts.profile

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    context: Context,
) : MviViewModel<ProfileViewModelState, ProfileViewModelEvents>(
    ProfileViewModelState(
        bluetoothState = BluetoothStatus.UNKNOWN,
        gattServerStatus = GattServerStatus.UNKNOWN,
        gattClientStatus = GattClientStatus.UNKNOWN,
        receivedData = "none"
    )
) {

    private val _updateBluetoothStatus: () -> Unit
    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val serviceUuid: UUID by lazy {
        UUID.nameUUIDFromBytes(
            byteArrayOf(
                1,
                2,
                3,
                4,
                5,
                6,
                7
            )
        )
    }
    private val characteristicUuid: UUID by lazy {
        UUID.nameUUIDFromBytes(
            byteArrayOf(
                7,
                6,
                5,
                4,
                3,
                2,
                1
            )
        )
    }

    private lateinit var bluetoothGattServer: BluetoothGattServer
    private var bluetoothGattClient: BluetoothGatt? = null
    private var connectedDevice: BluetoothDevice? = null

    init {
        _updateBluetoothStatus = {
            updateState {
                copy(
                    bluetoothState = fetchBluetoothStatus(context),
                    gattServerStatus = setupGattServer(context),
                )
            }
        }
    }

    fun updateBluetoothStatus() = _updateBluetoothStatus()

    private fun fetchBluetoothStatus(context: Context): BluetoothStatus {
        try {
            // Check if the device supports Bluetooth
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
        } catch (t: Throwable) {
            sendEvent(ProfileViewModelEvents.Error(t))

            return BluetoothStatus.ERROR
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupGattServer(context: Context): GattServerStatus {
        try {
            val gattServerCallback = object : BluetoothGattServerCallback() {
                override fun onConnectionStateChange(
                    device: BluetoothDevice,
                    status: Int,
                    newState: Int
                ) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        connectedDevice = device
                        setupGattClient(context, device)
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        connectedDevice = null
                    }
                }

                @SuppressLint("MissingPermission")
                override fun onCharacteristicReadRequest(
                    device: BluetoothDevice,
                    requestId: Int,
                    offset: Int,
                    characteristic: BluetoothGattCharacteristic
                ) {
                    try {
                        // Handle characteristic read requests
                        bluetoothGattServer.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            offset,
                            characteristic.value
                        )
                    } catch (t: Throwable) {
                        sendEvent(ProfileViewModelEvents.Error(t))
                    }
                }

                @SuppressLint("MissingPermission")
                override fun onCharacteristicWriteRequest(
                    device: BluetoothDevice,
                    requestId: Int,
                    characteristic: BluetoothGattCharacteristic,
                    preparedWrite: Boolean,
                    responseNeeded: Boolean,
                    offset: Int,
                    value: ByteArray
                ) {
                    try {
                        // Handle the written data here
                        // For example, converting the byte array to a readable format
                        // Convert to string if it's UTF-8 text
                        updateState { copy(receivedData = value.toString(Charsets.UTF_8)) }

                        if (responseNeeded) {
                            bluetoothGattServer.sendResponse(
                                device,
                                requestId,
                                BluetoothGatt.GATT_SUCCESS,
                                offset,
                                value
                            )
                        }
                    } catch (t: Throwable) {
                        sendEvent(ProfileViewModelEvents.Error(t))
                    }
                }
            }

            bluetoothGattServer = bluetoothManager.openGattServer(context, gattServerCallback)

            // Create a service and characteristic
            val service = BluetoothGattService(
                serviceUuid,
                BluetoothGattService.SERVICE_TYPE_PRIMARY
            )
            val characteristic = BluetoothGattCharacteristic(
                characteristicUuid,
                BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE
            )

            // Add characteristic to the service
            service.addCharacteristic(characteristic)

            // Add service to the GATT server
            if (bluetoothGattServer.addService(service)) {
                return GattServerStatus.ADDED
            } else {
                return GattServerStatus.FAILED
            }
        } catch (t: Throwable) {
            sendEvent(ProfileViewModelEvents.Error(t))
            return GattServerStatus.FAILED
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupGattClient(context: Context, device: BluetoothDevice) {
        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                try {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        gatt?.discoverServices()
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        bluetoothGattClient = null
                    }
                } catch (t: Throwable) {
                    sendEvent(ProfileViewModelEvents.Error(t))
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                try {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        updateState { copy(receivedData = gatt?.device?.toString() ?: "") }
                    }
                } catch (t: Throwable) {
                    sendEvent(ProfileViewModelEvents.Error(t))
                }
            }
        }

        try {
            bluetoothGattClient = device.connectGatt(context, false, gattCallback)
            updateState { copy(gattClientStatus = GattClientStatus.CONNECTED) }
        } catch (t: Throwable) {
            updateState { copy(gattClientStatus = GattClientStatus.FAILED) }
            sendEvent(ProfileViewModelEvents.Error(t))
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendSampleDataToServer(gatt: BluetoothGatt?) {
        val service = gatt?.getService(serviceUuid)
        val characteristic = service?.getCharacteristic(characteristicUuid)

        characteristic?.let {
            it.value = byteArrayOf(0x01, 0x02, 0x03) // Sample data
            gatt.writeCharacteristic(it)
        }
    }
}

data class ProfileViewModelState(
    val bluetoothState: BluetoothStatus,
    val gattServerStatus: GattServerStatus,
    val gattClientStatus: GattClientStatus,
    val receivedData: String,
) : MviViewState

sealed class ProfileViewModelEvents {
    data class Error(val t: Throwable) : ProfileViewModelEvents()
}