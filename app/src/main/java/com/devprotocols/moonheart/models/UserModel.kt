package com.devprotocols.moonheart.models

class UserModel {
     var name: String? = null
     var email: String? = null
     var userId: String? = null
     var age: String? = null
     var image: String? = null

    constructor()
    constructor(
        name: String?,
        email: String?,
        userId: String?,
        age: String?,
        image: String?
    ) {
        this.name = name
        this.email = email
        this.userId = userId
        this.age = age
        this.image = image
    }

}