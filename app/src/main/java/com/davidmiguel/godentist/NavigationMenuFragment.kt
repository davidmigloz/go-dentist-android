package com.davidmiguel.godentist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import com.davidmiguel.godentist.databinding.FragmentNavigationMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NavigationMenuFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNavigationMenuBinding

    private val args: NavigationMenuFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation_menu, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationView.setCheckedItem(args.currentItem)
        binding.navigationView.setNavigationItemSelectedListener {
            it.onNavDestinationSelected(findNavController())
        }
    }
}
