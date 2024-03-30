package domain

import Id

data class Album(
    val id: Id<Album>,
    val title: String,
    val artistId: Id<Artist>
)
