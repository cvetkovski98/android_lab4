package mk.com.ukim.finki.mpip.lab4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.com.ukim.finki.mpip.lab4.R
import mk.com.ukim.finki.mpip.lab4.databinding.FragmentStudentItemBinding
import mk.com.ukim.finki.mpip.lab4.model.Student

class StudentAdapter(
    private val students: MutableList<Student>,
    private val onEdit: (String) -> (Unit),
    private val onDelete: (String) -> (Unit),
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private lateinit var binder: FragmentStudentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binder = FragmentStudentItemBinding.inflate(inflater, parent, false)
        return StudentViewHolder(binder)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent: Student = students[position]

        binder.studentItemEditButton.setOnClickListener {
            onEdit(currentStudent.studentId)
        }

        binder.studentItemRemoveButton.setOnClickListener {
            onDelete(currentStudent.studentId)
        }

        holder.bind(currentStudent)
    }

    override fun getItemCount() = students.size

    fun setStudents(studentList: List<Student>) {
        students.clear()
        students.addAll(studentList)
    }

    inner class StudentViewHolder(
        private val studentItemBinding: FragmentStudentItemBinding,
    ) : RecyclerView.ViewHolder(studentItemBinding.root) {

        fun bind(student: Student) {
            studentItemBinding.studentItemId.text = student.studentId
            studentItemBinding.studentItemName.text = studentItemBinding.root.resources.getString(
                R.string.student_item_name_text,
                student.name,
                student.lastName
            )
            studentItemBinding.studentItemAddress.text = student.address
            studentItemBinding.studentItemMobilePhone.text = student.mobilePhone
        }

    }
}