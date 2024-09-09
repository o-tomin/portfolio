package com.oleksii.tomin.portfoliolayouts.profile

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentProfileBinding
import com.oleksii.tomin.portfoliolayouts.mvi.MviFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : MviFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val requestBluetoothConnectPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            viewModel.updateBluetoothStatus()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(viewModel) {
            collectStateProperty(ProfileViewModelState::bluetoothState) {
                binding.btStatus.text = it.name
            }
            collectStateProperty(ProfileViewModelState::gattServerStatus) {
                binding.gattServerStatus.text = it.name
            }

            collectEvents { event ->
                when (event) {
                    is ProfileViewModelEvents.Error -> Log.e("Alex", event.t.message, event.t)
                }
            }
        }

        checkAndRequestBluetoothConnectPermission()

        return binding.root
    }

    private fun checkAndRequestBluetoothConnectPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED -> {
                viewModel.updateBluetoothStatus()
            }

            shouldShowRequestPermissionRationale(BLUETOOTH_CONNECT) -> {
                requestBluetoothConnectPermission.launch(BLUETOOTH_CONNECT)
            }

            else -> {
                // Request permission directly
                requestBluetoothConnectPermission.launch(BLUETOOTH_CONNECT)
            }
        }
    }
}