package ru.spb.rollers.models

import com.google.firebase.database.Exclude

class User {
    var userId: String = ""
    var role: String = ""
    var userEmail: String? = ""
    var userPassword: String? = ""
    var userFirstName: String? = ""
    var userLastName: String? = ""
    var userStatus: String? = ""
    var userDistrict: String? = ""
    var userBirthday: String? = ""
    var userGender: String? = ""
    var userPhone: String? = ""
    var userPhoto: String? = ""

    var userSchoolName: String? = ""
    var userDescription: String? = ""
    var userAddress: String? = ""

    var isManager: Boolean = false

    constructor()
    constructor(role: String, userEmail: String?, userPassword: String?, isManager: Boolean) {
        this.role = role
        this.userEmail = userEmail
        this.userPassword = userPassword
        this.isManager = isManager
    }

    @Exclude
    fun  toMap() : Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "role" to role,
            "userEmail" to userEmail,
            "userPassword" to userPassword,
            "userFirstName" to userFirstName,
            "userLastName" to userLastName,
            "userStatus" to userStatus,
            "userDistrict" to userDistrict,
            "userBirthday" to userBirthday,
            "userGender" to userGender,
            "userPhone" to userPhone,
            "userPhoto" to userPhoto,
            "userSchoolName" to userSchoolName,
            "userDescription" to userDescription,
            "userAddress" to userAddress,
            "isManager" to isManager
        )
    }
}