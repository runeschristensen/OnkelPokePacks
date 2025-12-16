package com.onkelpoke.onkelpokeprod.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.onkelpoke.onkelpokeprod.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Her binder du UI til ViewModel
        viewModel.packs.observe(viewLifecycleOwner) { packs ->
            // opdater UI
        }

        binding.drawPackButton.setOnClickListener {
            val randomPack = viewModel.getRandomAvailablePack()
            // vis pack i UI
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}