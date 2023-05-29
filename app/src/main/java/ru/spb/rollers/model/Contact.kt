package ru.spb.rollers.model

class Contact {
    private var contactID: Int = 0
    var roleID: Int = 0
    var contactLogin: String? = ""
    var contactPassword: String? = ""
    var contactFirstName: String? = ""
    var contactLastName: String? = ""
    var contactPublicName: String? = ""
    var contactStatus: Boolean = false
    var contactDistrict: String? = ""
    var contactAge: String? = ""
    var contactGender: Boolean = false
    var isContact: Boolean = false

    var contactSchoolName: String? = ""
    var contactDescription: String? = ""
    var contactAddress: String? = ""

    var isManager: Boolean = false

    constructor(
        contactID: Int,
        roleID: Int,
        contactFirstName: String?,
        contactLastName: String?,
        contactPublicName: String?,
        contactStatus: Boolean,
        contactDistrict: String?,
        contactAge: String?,
        contactGender: Boolean,
        isContact: Boolean,
        isManager: Boolean
    ) {
        this.contactID = contactID
        this.roleID = roleID
        this.contactFirstName = contactFirstName
        this.contactLastName = contactLastName
        this.contactPublicName = contactPublicName
        this.contactStatus = contactStatus
        this.contactDistrict = contactDistrict
        this.contactAge = contactAge
        this.contactGender = contactGender
        this.isContact = isContact
        this.isManager = isManager
    }

    constructor()

    constructor(
        contactID: Int,
        roleID: Int,
        contactSchoolName: String?,
        contactPublicName: String?,
        contactDistrict: String?,
        isContact: Boolean,
        isManager: Boolean,
        contactDescription: String,
        contactAddress: String,
    ) {
        this.contactID = contactID
        this.roleID = roleID
        this.contactSchoolName = contactSchoolName
        this.contactPublicName = contactPublicName
        this.contactDistrict = contactDistrict
        this.isContact = isContact
        this.isManager = isManager
        this.contactDescription = contactDescription
        this.contactAddress = contactAddress
    }
    constructor(
        contactID: Int,
        roleID: Int,
        contactLogin: String? = "",
        contactPassword: String? = ""
    ) {
        this.contactID = contactID
        this.roleID = roleID
        this.contactLogin = contactLogin
        this.contactPassword = contactPassword

    }
}