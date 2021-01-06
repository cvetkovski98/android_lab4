package mk.com.ukim.finki.mpip.lab4.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Student(
    val studentId: String,
    val name: String,
    val lastName: String,
    val mobilePhone: String,
    val address: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        ""
    )

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "studentId" to studentId,
            "name" to name,
            "lastName" to lastName,
            "mobilePhone" to mobilePhone,
            "address" to address
        )
    }
}