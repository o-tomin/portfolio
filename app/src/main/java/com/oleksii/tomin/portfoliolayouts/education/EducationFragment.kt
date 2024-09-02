package com.oleksii.tomin.portfoliolayouts.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentEducationBinding

class EducationFragment : Fragment() {

    private lateinit var binding: FragmentEducationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEducationBinding.inflate(inflater, container, false)

        return binding.root
    }

}