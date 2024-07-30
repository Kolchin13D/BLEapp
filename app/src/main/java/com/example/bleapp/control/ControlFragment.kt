package com.example.bleapp.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bleapp.databinding.FragmentControlBinding
import com.example.bleapp.services.App

class ControlFragment : Fragment() {

    private var _binding: FragmentControlBinding? = null
    private val binding: FragmentControlBinding get() = _binding!!

    private val viewModel: ControlViewModel by viewModels {
        ControlViewModelFactory((requireActivity().application as App).adapterProvider)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentControlBinding.inflate(inflater, container, false)


        binding.apply {
            //nameDeviceFrag.text = _
        }

        return binding.root
    }


    companion object {

        private const val KEY_DEVICE_ADDRESS = "key_device_address"
        @JvmStatic
        fun  newInstance(deviceAddress: String) = ControlFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_DEVICE_ADDRESS, deviceAddress)
                }
            }

    }
}