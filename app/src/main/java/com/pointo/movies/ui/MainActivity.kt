package com.pointo.movies.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pointo.movies.R
import com.pointo.movies.databinding.ActivityMainBinding
import com.pointo.movies.util.Constants
import com.pointo.movies.util.hide
import com.pointo.movies.util.show
import com.skydoves.bindables.BindingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val vm: MainViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var inflater: NavInflater
    private lateinit var graph: NavGraph


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding {
            lifecycleOwner = this@MainActivity
            viewModel = vm
        }

        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.nav_graph)

        vm.loginLiveData.observe(this) {
            if (it?.loginRequired == null || it.loginRequired) {//No Login Data so open login page
                openLoginPage()
            } else {//Open Home page
                openHomePage(it.username)
            }
            vm.loginLiveData.removeObservers(this)

        }
    }

    fun openHomePage(username: String?) {
        val bundle = Bundle()
        bundle.putString(Constants.argKeyEmail, username)
        graph.setStartDestination(R.id.searchFragment)
        navHostFragment.navController.setGraph(graph, bundle)
        setupBottomNavigation()
    }

    private fun openLoginPage() {
        graph.setStartDestination(R.id.loginFragment)
        navHostFragment.navController.graph = graph
        binding.bottomNavigation.hide()

    }

    override fun onBackPressed() {
        if (navHostFragment.navController.currentDestination?.id ?: 0 == R.id.detailFragment) {
            navHostFragment.navController.popBackStack()
            binding.bottomNavigation.show()
        } else {
            if (binding.bottomNavigation.selectedItemId == R.id.searchFragment) {
                super.onBackPressed()
            } else {
                binding.bottomNavigation.selectedItemId = R.id.searchFragment
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(findNavController(R.id.nav_host_fragment))
        binding.bottomNavigation.setOnItemReselectedListener { }
        binding.bottomNavigation.show()

        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.detailFragment, R.id.loginFragment -> {
                    binding.bottomNavigation.hide()
                }
                else -> {
                    binding.bottomNavigation.show()
                }
            }
        }
    }
}