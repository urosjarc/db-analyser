package domain

import Id

data class Playlist(
    val id: Id<Playlist>,
    val name: String
)
