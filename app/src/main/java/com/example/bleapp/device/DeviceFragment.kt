package com.example.bleapp.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bleapp.R
import com.example.bleapp.control.ControlFragment
import com.example.bleapp.databinding.FragmentDevicesBinding

class DeviceFragment : Fragment(), DeviceAdapter.Callback {

    private var _binding: FragmentDevicesBinding? = null
    private val binding: FragmentDevicesBinding get() = _binding!!

    private val devicesAdapter = DeviceAdapter()

    private val viewModel: DeviceViewModel by viewModels(

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInctance() = DeviceFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDevices.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = devicesAdapter
        }

        devicesAdapter.addCallback(this)

        binding.fabStartScan.setOnClickListener {
            checkLocation.launch(android.Manifest.permission.BLUETOOTH_SCAN)
        }

        binding.fabStopScan.setOnClickListener {
            onStop()
        }

    }

    override fun onStart() {
        super.onStart()
        subscribeOnViewModel()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopScan()
    }

    private fun subscribeOnViewModel() {
        viewModel.devices.observe(viewLifecycleOwner, { devices ->
            devicesAdapter.update(devices)
        })
    }

    @SuppressLint("MissingPermission")
    override fun onItemClick(device: BluetoothDevice) {
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.containerFragment, ControlFragment.newInstance(device.address))
            .commit()

        Toast.makeText(requireContext(), "connect to device ${device.name}", Toast.LENGTH_SHORT).show()
    }

    private val checkLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.startScan()
        } else {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}


