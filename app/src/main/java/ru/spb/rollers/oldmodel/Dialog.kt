package ru.spb.rollers.oldmodel

class Dialog {
    var dialogID: Int = 0
    var dialogName: String? = ""
    var dialogDate: String? = ""
    var dialogMessage: String? = ""

    constructor(_DialogID: Int, _DialogName: String, _DialogDate: String, _DialogMessage: String){
        this.dialogID = _DialogID
        this.dialogName = _DialogName
        this.dialogDate = _DialogDate
        this.dialogMessage = _DialogMessage
    }
}