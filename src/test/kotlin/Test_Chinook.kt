import java.io.File
import kotlin.test.Test

class Test_Chinook {
    @Test
    fun `test db2`() {
        File("build/db2.plantuml").writeText(db2_serializer.plantUML())
        Seed.db2()
    }

    @Test
    fun `test derby`() {
        File("build/derby.plantuml").writeText(derby_serializer.plantUML())
        Seed.derby()
    }

    @Test
    fun `test h2`() {
        File("build/h2.plantuml").writeText(h2_serializer.plantUML())
        Seed.h2()
    }

    @Test
    fun `test maria`() {
        File("build/maria.plantuml").writeText(maria_serializer.plantUML())
        Seed.maria()
    }

    @Test
    fun `test mysql`() {
        File("build/mysql.plantuml").writeText(mysql_serializer.plantUML())
        Seed.mysql()
    }

    @Test
    fun `test mssql`() {
        File("build/mssql.plantuml").writeText(mssql_serializer.plantUML())
        Seed.mssql()
    }

    @Test
    fun `test postgresql`() {
        File("build/postgresql.plantuml").writeText(postgresql_serializer.plantUML())
        Seed.pg()
    }

    @Test
    fun `test oracle`() {
        File("build/oracle.plantuml").writeText(oracle_serializer.plantUML())
        Seed.oracle()
    }

    @Test
    fun `test sqlite`() {
        File("build/sqlite.plantuml").writeText(sqlite_serializer.plantUML())
        Seed.sqlite()
    }
}
