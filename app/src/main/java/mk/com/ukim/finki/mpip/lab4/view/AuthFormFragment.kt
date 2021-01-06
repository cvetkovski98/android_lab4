package mk.com.ukim.finki.mpip.lab4.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.com.ukim.finki.mpip.lab4.databinding.FragmentAuthFormBinding

class AuthFormFragment : Fragment() {

    private lateinit var binding: FragmentAuthFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthFormBinding.inflate(inflater, container, false)
        return binding.root
    }
}