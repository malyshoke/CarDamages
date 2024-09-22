package com.gap.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gap.presentation.main.HomeFragment

class SplashScreenFragment : Fragment() {
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: working")
        navController.navigate(R.id.sendPhotosFragment)
//        workWithSharedPreferences()
    }

    private fun workWithSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            HomeFragment.START_FRAGMENT_TAG,
            Context.MODE_PRIVATE
        )
        val isReachedHomeFragment = sharedPreferences.getBoolean(
            HomeFragment.REACHED_HOME_FRAGMENT,
            false
        )
        if (isReachedHomeFragment) {
            navController.navigate(R.id.homeFragment)
        } else {
            navController.navigate(R.id.startFragment)
        }
    }

    companion object {
        private const val TAG = "SplashScreenFragment"
    }

}