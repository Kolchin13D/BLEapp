package com.example.bleapp.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bleapp.databinding.DeviceAdapterBinding

class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private val items = mutableListOf<BluetoothDevice>()
    private var callback: Callback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<BluetoothDevice>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = DeviceAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return DeviceViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class DeviceViewHolder(val binding: DeviceAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("MissingPermission")
        fun bind(item: BluetoothDevice) {

            binding.container.setOnClickListener { callback?.onItemClick(item) }

            binding.apply {
                nameDevice.text = item.name ?: "Null"
                addressDevice.text = item.address
            }
        }

    }

    fun addCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onItemClick(device: BluetoothDevice)
    }

}