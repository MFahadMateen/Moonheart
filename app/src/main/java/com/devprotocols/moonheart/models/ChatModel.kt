package com.devprotocols.moonheart.models

class ChatModel {
    var chatId: String? = null
    var lastMessage:String?= null
    var timestamp:Long?= null
    var messages: MessageModel?= null
    var members: ArrayList<String>?= ArrayList()

    constructor()
    constructor(
        chatId: String?,
        lastMessage: String?,
        timestamp: Long?,
        members: ArrayList<String>?
    ) {
        this.chatId = chatId
        this.lastMessage = lastMessage
        this.timestamp = timestamp
        this.members = members
    }


}