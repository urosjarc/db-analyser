package domain

import Id

data class PlaylistTrack(
    val id: Id<PlaylistTrack>,
    val playlistId: Id<Playlist>,
    val trackId: Id<Track>
)
