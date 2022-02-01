package com.example.tonote.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tonote.R
import com.example.tonote.databinding.FragmentOpenNoteBinding
import com.example.tonote.databinding.FragmentPasswordBinding
import com.example.tonote.util.LocalKeyStorage
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hanks.passcodeview.PasscodeView


class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    lateinit var localKeyStorage: LocalKeyStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        localKeyStorage = LocalKeyStorage(requireContext())

        binding.passcodeView.apply {
            if (localKeyStorage.getValue(LocalKeyStorage.passcode)==0){
                passcodeType = PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE
                listener = object : PasscodeView.PasscodeViewListener{
                    override fun onFail() {
                        //Toast.makeText(context,"Please match passcode",Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(number: String?) {
                        if (number != null) {
                            localKeyStorage.saveValue(LocalKeyStorage.passcode,number.toInt())
                            //Toast.makeText(context,"Success to",Toast.LENGTH_SHORT).show()
                            // Now navigate to another fragment
                            findNavController().navigate(R.id.action_passwordFragment_to_hiddenNoteFragment)
                        }
                    }

                }
            }
            else{
                passcodeType = PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE
                localPasscode = localKeyStorage.getValue(LocalKeyStorage.passcode).toString()
                listener = object : PasscodeView.PasscodeViewListener{
                    override fun onFail() {
                        //Toast.makeText(context,"Please enter the correct passcode",Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(number: String?) {
                        //Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                        // Navigate to another fragment
                        findNavController().navigate(R.id.action_passwordFragment_to_hiddenNoteFragment)
                    }
                }
            }
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}

