package com.oleksii.tomin.portfoliolayouts.experience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oleksii.tomin.portfoliolayouts.databinding.FragmentExperienceBinding

class ExperienceFragment : Fragment() {

    private lateinit var binding: FragmentExperienceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExperienceBinding.inflate(inflater, container, false)

        return binding.root
    }

}