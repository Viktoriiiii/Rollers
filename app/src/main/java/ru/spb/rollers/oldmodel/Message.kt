package ru.spb.rollers.oldmodel

class Message {
    var messageID: Int = 0
    var messageDate: String? = ""
    var messageText: String? = ""
    var messageSender: String? = ""
    var userID: Int = 0

    constructor(
        messageID: Int,
        messageDate: String?,
        messageText: String?,
        messageSender: String?,
        userID: Int
    ) {
        this.messageID = messageID
        this.messageDate = messageDate
        this.messageText = messageText
        this.messageSender = messageSender
        this.userID = userID
    }
}