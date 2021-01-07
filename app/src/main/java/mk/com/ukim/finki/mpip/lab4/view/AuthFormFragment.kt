package mk.com.ukim.finki.mpip.lab4.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import mk.com.ukim.finki.mpip.lab4.databinding.FragmentAuthFormBinding
import mk.com.ukim.finki.mpip.lab4.model.Credentials
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.model.Status
import mk.com.ukim.finki.mpip.lab4.util.FactoryInjector
import mk.com.ukim.finki.mpip.lab4.viewmodel.AuthViewModel

class AuthFormFragment : Fragment() {

    private lateinit var binding: FragmentAuthFormBinding
    private val authViewModel: AuthViewModel by activityViewModels {
        FactoryInjector.getAuthViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initButtons()

        val navigateBackOrError: (Resource<FirebaseUser>) -> (Unit) = {
            when (it.status) {
                Status.SUCCESS -> {
                    redirectToList()
                }

                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Status.LOADING -> { // do nothing
                }
            }
        }

        authViewModel.currentUser.observe(viewLifecycleOwner, navigateBackOrError)
    }

    private fun redirectToList() {
        val action = AuthFormFragmentDirections
            .actionAuthFormFragmentToStudentListFragment()
        findNavController().navigate(action)
    }

    private fun initButtons() {

        binding.signInButton.setOnClickListener {
            it.hideKeyboard()
            val credentials: Credentials? = getCredentials()
            if (credentials != null) {
                authViewModel.signInUser(credentials)
            } else {
                Toast.makeText(
                    context,
                    "Email or password is blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signUpButton.setOnClickListener {
            it.hideKeyboard()
            val credentials: Credentials? = getCredentials()
            if (credentials != null) {
                authViewModel.signUpUser(credentials)
            } else {
                Toast.makeText(
                    context,
                    "Email or password is blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getCredentials(): Credentials? {
        val email: String = binding.emailInput.text.toString()
        val password: String = binding.passwordInput.text.toString()

        return if (email.isBlank() || password.isBlank()) {
            null
        } else {
            Credentials(
                email, password
            )
        }
    }

}