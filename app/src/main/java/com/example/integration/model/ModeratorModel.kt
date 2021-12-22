package com.example.integration.model

class ModeratorModel(
    val id: String = "report00",
    val nomDepot: String = "Nom Dépot",
    val email: String = "contactadmin@citoyapp.com",
    var modAssignement: String = "moderator@citoyapp.com",
    var pinned: Boolean = false
)