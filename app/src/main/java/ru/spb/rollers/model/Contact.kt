package ru.spb.rollers.model

class Contact {
    private var contactID: Int = 0
    var contactName: String? = ""
    var contactStatus: Boolean = false
    var contactDistrict: String? = ""
    var contactAge: String? = ""
    var isContact: Boolean = false

    constructor(
        contactID: Int,
        contactName: String?,
        contactStatus: Boolean,
        contactDistrict: String?,
        contactAge: String?,
        isContact: Boolean
    ) {
        this.contactID = contactID
        this.contactName = contactName
        this.contactStatus = contactStatus
        this.contactDistrict = contactDistrict
        this.contactAge = contactAge
        this.isContact = isContact
    }
}