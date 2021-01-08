package mk.com.ukim.finki.mpip.lab4.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val studentEditId: String? = args.studentEditId
        val missingDataToast: Toast = Toast.makeText(
            context,
            "Please fill all fields",
            Toast.LENGTH_SHORT
        )

        studentEditId?.let {
            // edit id exists
            studentViewModel.fetchStudentDetails(it)
            binding.createUserButton.setOnClickListener {
                val studentData = toStudent()
                if (studentData == null) {
                    missingDataToast.show()
                } else {
                    studentViewModel.updateStudent(studentData)
                }
            }
        } ?: run {
            binding.detailsProgress.visibility = View.INVISIBLE
            binding.createUserButton.setOnClickListener {
                val studentData = toStudent()
                if (studentData == null) {
                    missingDataToast.show()
                } else {
                    studentViewModel.createStudent(studentData)
                }
            }
        }

        studentViewModel.studentDetails.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    fillForm(it.data)
                    binding.detailsProgress.visibility = View.INVISIBLE
                }

                Status.ERROR -> {
                    // do nothing
                    binding.detailsProgress.visibility = View.INVISIBLE
                }

                Status.LOADING -> {
                    binding.detailsProgress.visibility = View.VISIBLE
                }
            }
        })

        studentViewModel.studentCreateStatus.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    findNavController()
                        .popBackStack()
                }

                else -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        studentViewModel.studentUpdateStatus.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    findNavController()
                        .popBackStack()
                }

                else -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun toStudent(): Student? {
        val name: String = binding.studentNameInput.text.toString()
        val lastName: String = binding.studentLastNameInput.text.toString()
        val address: String = binding.studentAddressInput.text.toString()
        val index: String = binding.studentIndexInput.text.toString()
        val mobilePhone: String = binding.studentPhoneNumberInput.text.toString()

        if (name.isBlank() || lastName.isBlank() || address.isBlank() || index.isBlank() || mobilePhone.isBlank()) {
            return null
        }

        return Student(
            index,
            name,
            lastName,
            mobilePhone,
            address
        )
    }
}
