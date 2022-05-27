package com.devprotocols.moonheart.models

import java.sql.Timestamp

class MessageModel {
    var messageId:String?= null
    var message:String?= null
    var sendBy:String?= null
    var time: Long?=null

    constructor()
    constructor(messageId: String?, message: String?, sendBy: String?, time: Long?) {
        this.messageId = messageId
        this.message = message
        this.sendBy = sendBy
        this.time = time
    }

}