package com.pointo.movies.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pointo.movies.R
import com.pointo.movies.databinding.FragmentLoginBinding
import com.pointo.movies.ui.MainActivity
import com.pointo.movies.util.observeInLifecycle
import com.pointo.movies.util.shortToast
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val vm: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            lifecycleOwner = this@LoginFragment
            viewModel = vm
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentLoginEtEmail.addTextChangedListener {
            binding.fragmentLoginTilEmail.error = null
        }
        binding.fragmentLoginEtPassword.addTextChangedListener {
            binding.fragmentLoginTilPassword.error = null
        }

        binding.fragmentLoginBtnLoginWebsite.setOnClickListener {
            with(vm) {
                email.set(getString(R.string.dummy_email))
                saveLoginInfoToDatabase()
            }
        }

        vm.eventsFlow.onEach {
            when (it) {
                is LoginViewModel.Event.NavigateToChat -> {
                    val email = binding.fragmentLoginEtEmail.text.toString()
                    (activity as MainActivity).openHomePage(email)
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToSearchFragment(email)
                    )
                    shortToast("Welcome $email")
                }
                is LoginViewModel.Event.ShowPasswordNoUppercaseError -> {
                    binding.fragmentLoginTilPassword.error =
                        getString(R.string.error_login_password_uppercase)
                }
                is LoginViewModel.Event.ShowPasswordLengthError -> {
                    binding.fragmentLoginTilPassword.error =
                        getString(R.string.error_login_password_length)
                }
                is LoginViewModel.Event.ShowPasswordNoDigitError -> {
                    binding.fragmentLoginTilPassword.error =
                        getString(R.string.error_login_password_digit)
                }
                is LoginViewModel.Event.ShowNoEmailError -> {
                    binding.fragmentLoginTilEmail.error =
                        getString(R.string.error_login_no_email)
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }
}