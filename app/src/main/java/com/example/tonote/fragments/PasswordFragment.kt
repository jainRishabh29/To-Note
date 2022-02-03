package com.example.tonote.fragments

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import java.util.ArrayList


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
                            if(localKeyStorage.getValue(LocalKeyStorage.isFromOtherFragment)==1){
                                val contentOfNote = arguments?.getStringArrayList("Content")
                                val contentArray: ArrayList<String> = ArrayList()
                                val bundle = Bundle()
                                if(!contentOfNote.isNullOrEmpty()){
                                    contentArray.add(0, contentOfNote[0])
                                    contentArray.add(1, contentOfNote[1])
                                    contentArray.add(2, contentOfNote[2])
                                    contentArray.add(3, contentOfNote[3])
                                    contentArray.add(4, contentOfNote[4])
                                    contentArray.add(5, contentOfNote[5])
                                    contentArray.add(6, "true")
                                }
                                bundle.putStringArrayList("Content", contentArray)
                                findNavController().navigate(R.id.action_passwordFragment_to_openNoteFragment,bundle)
                            }else {
                                findNavController().navigate(R.id.action_passwordFragment_to_hiddenNoteFragment)
                            }
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

