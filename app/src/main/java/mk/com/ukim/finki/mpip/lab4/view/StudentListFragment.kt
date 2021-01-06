package mk.com.ukim.finki.mpip.lab4.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mk.com.ukim.finki.mpip.lab4.adapter.StudentAdapter
import mk.com.ukim.finki.mpip.lab4.databinding.FragmentStudentListBinding
import mk.com.ukim.finki.mpip.lab4.model.Status
import mk.com.ukim.finki.mpip.lab4.model.Student
import mk.com.ukim.finki.mpip.lab4.util.FactoryInjector
import mk.com.ukim.finki.mpip.lab4.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {

    private lateinit var binding: FragmentStudentListBinding
    private lateinit var studentAdapter: StudentAdapter
    private val studentViewModel: StudentViewModel by viewModels {
        FactoryInjector.getStudentViewModel()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initFab()
        initRecycler(listOf())
        studentViewModel.fetchStudentList()
        studentViewModel.getStudents().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> it.data?.let { data ->
                    updateAdapterData(data)
                }

                Status.ERROR -> Toast.makeText(
                    context,
                    "There was an error fetching students",
                    Toast.LENGTH_SHORT
                ).show()

                Status.LOADING -> { // do nothing
                }
            }
        })

        studentViewModel.getRemoveStatus().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        context,
                        "Student successfully removed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        context,
                        "ERROR",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    private fun initRecycler(studentList: List<Student>) {
        studentAdapter = StudentAdapter(
            studentList.toMutableList(),
            { onEditId -> redirectToEditForm(onEditId) },
            { onDeleteId -> studentViewModel.removeStudent(onDeleteId) }
        )

        val llm = LinearLayoutManager(context)

        binding.studentList.apply {
            adapter = studentAdapter
            layoutManager = llm
        }

        Log.i(TAG, "onActivityCreated: $studentList")
    }

    private fun updateAdapterData(studentList: List<Student>) {
        studentAdapter.setStudents(studentList)
        studentAdapter.notifyDataSetChanged()
    }

    private fun initFab() {
        val action = StudentListFragmentDirections
            .actionStudentListFragmentToStudentFormFragment(null)

        binding.fab.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    private fun redirectToEditForm(studentEditId: String) {
        val action = StudentListFragmentDirections
            .actionStudentListFragmentToStudentFormFragment(studentEditId)
        Log.i(TAG, "redirectToEditForm: $studentEditId")
        findNavController().navigate(action)
    }

    override fun onPause() {
        super.onPause()
        studentViewModel.getRemoveStatus()
    }

    companion object {
        private const val TAG = "StudentListFragment"
    }

}