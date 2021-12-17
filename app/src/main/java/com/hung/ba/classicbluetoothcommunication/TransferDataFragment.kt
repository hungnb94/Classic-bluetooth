package com.hung.ba.classicbluetoothcommunication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hung.ba.classicbluetoothcommunication.databinding.TransferDataFragmentBinding
import java.text.DecimalFormat

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
        val numberFormat = DecimalFormat.getPercentInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        viewModel.percentTransfer.observe(viewLifecycleOwner, { percent ->
            binding?.tvPercent?.run {
                val percentStr = numberFormat.format(percent)
                if (percentStr !== text) {
                    text = percentStr
                }
            }
        })
        viewModel.readingFile.observe(viewLifecycleOwner, { reading ->
            binding?.progressReadingFile?.run {
                visibility = if (reading) View.VISIBLE
                else View.GONE
            }
        })
        viewModel.transferDuration.observe(viewLifecycleOwner, { duration ->
            binding?.tvDuration?.run {
                val minutes = duration / 1000 / 60
                val seconds = (duration / 1000 % 60)
                text = String.format("%02d:%02d", minutes, seconds)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}