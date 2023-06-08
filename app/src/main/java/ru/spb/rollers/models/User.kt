package ru.spb.rollers.models

import com.google.firebase.database.Exclude

data class User(
    var id: String = "",
    var role: String = "",
    var email: String? = "",
    var password: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var status: String? = "",
    var district: String? = "",
    var birthday: String? = "",
    var gender: String? = "",
    var phone: String? = "",
    var photo: String? = "",
    var schoolName: String? = "",
    var description: String? = "",
    var address: String? = "",
    var isManager: Boolean = false
) {
    constructor(role: String, userEmail: String?, userPassword: String?, isManager: Boolean) :
            this("", role, userEmail, userPassword, "", "", "", "", "", "", "", "", "", "", "", isManager)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "role" to role,
            "email" to email,
            "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "status" to status,
            "district" to district,
            "birthday" to birthday,
            "gender" to gender,
            "phone" to phone,
            "photo" to photo,
            "schoolName" to schoolName,
            "description" to description,
            "address" to address,
            "manager" to isManager
        )
    }
}