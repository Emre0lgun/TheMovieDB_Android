package com.pointo.movies.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.pointo.movies.R
import com.pointo.movies.databinding.ActivityMainBinding
import com.skydoves.bindables.BindingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding {
            lifecycleOwner = this@MainActivity
            viewModel = vm
        }
    }
}