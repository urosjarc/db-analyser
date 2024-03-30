package domain

import Id

data class Genre(
    val id: Id<Genre>,
    val name: String
)
