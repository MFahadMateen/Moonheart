package com.devprotocols.moonheart.models

class SettingsModel {
     var image: Int? = null
     var name: String? = null

    constructor()
    constructor(image: Int?, name: String?) {
        this.image = image
        this.name = name
    }
}