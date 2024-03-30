import domain.*
import java.time.LocalDate
import kotlin.random.Random

/**
 * The [Seed] class is responsible for populating the database with sample data,
 * that is the same as famous [chinook sample database](https://github.com/lerocha/chinook-database).
 */
object Seed {
    val seedSize = 20
    val rseed = Random(seed = 0)

    val boss = Employee(
        id = Id(1),
        firstName = "firstName_00",
        lastName = "lastName_00",
        title = "title_00",
        reportsTo = null,
        birthDate = LocalDate.now(),
        hireDate = LocalDate.now()
    )

    val artist = arrayOfNulls<Album>(seedSize).mapIndexed { i, _ -> Artist(id = Id(i + 1), name = "name_$i") }
    val album = arrayOfNulls<Album>(seedSize).mapIndexed { i, _ -> Album(id = Id(i + 1), title = "name_$i", artistId = artist.random(rseed).id!!) }
    val mediaType = arrayOfNulls<MediaType>(seedSize).mapIndexed { i, _ -> MediaType(id = Id(i + 1), name = "name_$i") }
    val genre = arrayOfNulls<Genre>(seedSize).mapIndexed { i, _ -> Genre(id = Id(i + 1), name = "name_$i") }
    val playlist = arrayOfNulls<Playlist>(seedSize).mapIndexed { i, _ -> Playlist(id = Id(i + 1), name = "name_$i") }

    val employee = arrayOfNulls<Employee>(seedSize).mapIndexed { i, _ ->
        Employee(
            id = Id(i + 2),
            firstName = "firstName_$i",
            lastName = "lastName_$i",
            title = "title_$i",
            reportsTo = boss.id,
            birthDate = LocalDate.now(),
            hireDate = LocalDate.now()
        )
    }

    val customer = arrayOfNulls<Customer>(seedSize).mapIndexed { i, _ ->
        Customer(
            id = Id(i + 1),
            firstName = "firstName_$i",
            lastName = "lastName_$i",
            supportRepId = employee.random(rseed).id
        )
    }

    val invoice = arrayOfNulls<Invoice>(seedSize).mapIndexed { i, _ ->
        Invoice(
            id = Id(i + 1),
            customerId = customer.random(rseed).id,
            invoiceDate = LocalDate.now(),
            billingAddress = "billingAddress_$i"
        )
    }

    val track = arrayOfNulls<Track>(seedSize).mapIndexed { i, _ ->
        Track(
            id = Id(i + 1),
            name = "name_$i",
            albumId = album.random(rseed).id,
            mediaTypeId = mediaType.random(rseed).id,
            genreId = genre.random(rseed).id,
            composer = "composer_id"
        )
    }

    val playlistTrack = arrayOfNulls<PlaylistTrack>(seedSize).mapIndexed { i, _ ->
        PlaylistTrack(
            id = Id(i + 1),
            playlistId = playlist.random(rseed).id,
            trackId = track.random(rseed).id
        )
    }

    val invoiceLine = arrayOfNulls<InvoiceLine>(seedSize).mapIndexed { i, _ ->
        InvoiceLine(
            id = Id(i + 1),
            invoiceId = invoice.random(rseed).id,
            trackId = track.random(rseed).id,
            unitPrice = i.toFloat(),
            quantity = i
        )
    }

    fun db2() = db2.autocommit {
        it.schema.create(schema = db2_people_schema, throws = false)
        it.schema.create(schema = db2_music_schema, throws = false)
        it.schema.create(schema = db2_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(rows = artist)
        it.batch.insert(rows = album)
        it.batch.insert(rows = mediaType)
        it.batch.insert(rows = genre)
        it.batch.insert(rows = playlist)
        it.row.insert(boss)
        it.batch.insert(rows = employee)
        it.batch.insert(rows = customer)
        it.batch.insert(rows = invoice)
        it.batch.insert(rows = track)
        it.batch.insert(rows = playlistTrack)
        it.batch.insert(rows = invoiceLine)
    }

    fun derby() = derby.autocommit {
        it.schema.create(schema = derby_people_schema, throws = false)
        it.schema.create(schema = derby_music_schema, throws = false)
        it.schema.create(schema = derby_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun h2() = h2.autocommit {
        it.schema.create(schema = h2_people_schema, throws = false)
        it.schema.create(schema = h2_music_schema, throws = false)
        it.schema.create(schema = h2_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun maria() = maria.autocommit {
        it.schema.create(schema = maria_people_schema, throws = false)
        it.schema.create(schema = maria_music_schema, throws = false)
        it.schema.create(schema = maria_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun mssql() = mssql.autocommit {
        it.schema.create(schema = mssql_billing_schema, throws = false)
        it.schema.create(schema = mssql_people_schema, throws = false)
        it.schema.create(schema = mssql_music_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
    }

    fun mysql() = mysql.autocommit {
        it.schema.create(schema = mysql_people_schema, throws = false)
        it.schema.create(schema = mysql_music_schema, throws = false)
        it.schema.create(schema = mysql_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun sqlite() = sqlite.autocommit {
        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun pg() = pg.autocommit {
        it.schema.create(schema = pg_people_schema, throws = false)
        it.schema.create(schema = pg_music_schema, throws = false)
        it.schema.create(schema = pg_billing_schema, throws = false)

        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

    fun oracle() = oracle.autocommit {
        it.table.create<Artist>(throws = false)
        it.table.create<Album>(throws = false)
        it.table.create<MediaType>(throws = false)
        it.table.create<Genre>(throws = false)
        it.table.create<Playlist>(throws = false)
        it.table.create<Employee>(throws = false)
        it.table.create<Customer>(throws = false)
        it.table.create<Invoice>(throws = false)
        it.table.create<Track>(throws = false)
        it.table.create<PlaylistTrack>(throws = false)
        it.table.create<InvoiceLine>(throws = false)

        it.table.delete<Artist>()
        it.table.delete<Album>()
        it.table.delete<MediaType>()
        it.table.delete<Genre>()
        it.table.delete<Playlist>()
        it.table.delete<Employee>()
        it.table.delete<Customer>()
        it.table.delete<Invoice>()
        it.table.delete<Track>()
        it.table.delete<PlaylistTrack>()
        it.table.delete<InvoiceLine>()

        it.batch.insert(artist)
        it.batch.insert(album)
        it.batch.insert(mediaType)
        it.batch.insert(genre)
        it.batch.insert(playlist)
        it.row.insert(boss)
        it.batch.insert(employee)
        it.batch.insert(customer)
        it.batch.insert(invoice)
        it.batch.insert(track)
        it.batch.insert(playlistTrack)
        it.batch.insert(invoiceLine)
    }

}
