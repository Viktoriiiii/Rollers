package ru.spb.rollers.models

class User {
    var userId: String = ""
    var role: String = ""
    var userEmail: String? = ""
    var userPassword: String? = ""
    var userFirstName: String? = ""
    var userLastName: String? = ""
    var userPublicName: String? = ""
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
}