package mk.com.ukim.finki.mpip.lab4.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import mk.com.ukim.finki.mpip.lab4.databinding.FragmentStudentFormBinding
import mk.com.ukim.finki.mpip.lab4.model.Status
import mk.com.ukim.finki.mpip.lab4.model.Student
import mk.com.ukim.finki.mpip.lab4.util.FactoryInjector
import mk.com.ukim.finki.mpip.lab4.viewmodel.StudentViewModel

class StudentFormFragment : Fragment() {

    private lateinit var binding: FragmentStudentFormBinding
    private val args: StudentFormFragmentArgs by navArgs()
    private val studentViewModel: StudentViewModel by viewModels {
        FactoryInjector.getStudentViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentFormBinding.inflate(inflater, container, false)
        Log.i(TAG, "onCreateView: here")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val studentEditId: String? = args.studentEditId

        studentEditId?.let {
            // edit id exists
            studentViewModel.fetchStudentDetails(it)
            binding.createUserButton.setOnClickListener {
                studentViewModel.updateStudent(toStudent())
                findNavController().popBackStack()
            }
        } ?: run {
            binding.createUserButton.setOnClickListener {
                studentViewModel.createStudent(toStudent())
                findNavController().popBackStack()
            }
        }

        studentViewModel.getStudentDetails().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    fillForm(it.data)
                }

                else -> { // do nothing
                }
            }
        })
    }

    private fun fillForm(data: Student?) {
        data?.let {
            binding.studentIndexInput.setText(data.studentId)
            binding.studentNameInput.setText(data.name)
            binding.studentLastNameInput.setText(data.lastName)
            binding.studentPhoneNumberInput.setText(data.mobilePhone)
            binding.studentAddressInput.setText(data.address)
        }
    }

    private fun toStudent(): Student {
        val name: String = binding.studentNameInput.text.toString()
        val lastName: String = binding.studentLastNameInput.text.toString()
        val address: String = binding.studentAddressInput.text.toString()
        val index: String = binding.studentIndexInput.text.toString()
        val mobilePhone: String = binding.studentPhoneNumberInput.text.toString()

        return Student(
            index,
            name,
            lastName,
            mobilePhone,
            address
        )
    }

    companion object {
        private const val TAG = "StudentFormFragment"
    }
}