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
        isContact: Boolean
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
    }

    constructor()
}