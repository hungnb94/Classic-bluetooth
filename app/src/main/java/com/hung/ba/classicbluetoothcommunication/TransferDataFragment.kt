package com.hung.ba.classicbluetoothcommunication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hung.ba.classicbluetoothcommunication.databinding.TransferDataFragmentBinding

class TransferDataFragment : Fragment() {

    companion object {
        fun newInstance() = TransferDataFragment()
    }

    private lateinit var viewModel: TransferDataViewModel
    private var binding: TransferDataFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransferDataFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TransferDataViewModel::class.java]
        binding?.model = viewModel
        viewModel.readData(requireActivity().assets.open("XiaomiRawBytes.txt"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}