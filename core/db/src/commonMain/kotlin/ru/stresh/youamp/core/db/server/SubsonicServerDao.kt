package ru.stresh.youamp.core.db.server

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SubsonicServerDao {

    @Query("SELECT * FROM server_subsonic WHERE isActive = 1")
    fun flowActive(): Flow<SubsonicServerDb?>

    @Query("SELECT COUNT(*) FROM server_subsonic WHERE id != :id")
    suspend fun getCountExcept(id: Long): Int

    @Query("SELECT COUNT(*) FROM server_subsonic")
    suspend fun getCount(): Int

    @Query("SELECT * FROM server_subsonic WHERE id = :serverId")
    suspend fun getServer(serverId: Long): SubsonicServerDb?

    @Query("SELECT * FROM server_subsonic WHERE isActive = 1")
    suspend fun getActive(): SubsonicServerDb?

    @Query("SELECT * FROM server_subsonic WHERE id = :id")
    suspend fun getSettings(id: Long): SubsonicServerDb?

    @Query("UPDATE server_subsonic SET isActive = 1 WHERE id = :id")
    suspend fun activate(id: Long)

    @Query("UPDATE server_subsonic SET isActive = 0")
    suspend fun deactivateAll()

    @Query("DELETE FROM server_subsonic WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM server_subsonic")
    fun flowAll(): Flow<List<SubsonicServerDb>>

    @Query("SELECT * FROM server_subsonic")
    suspend fun getAll(): List<SubsonicServerDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(server: SubsonicServerDb): Long

    @Delete
    suspend fun delete(server: SubsonicServerDb)

    @Transaction
    suspend fun delete(id: Long) {
        remove(id)
        val all = getAll()
        val active = all.firstOrNull { it.isActive }
        if (active == null) {
            val first = all.firstOrNull() ?: return
            setActive(first)
        }
    }

    @Transaction
    suspend fun setActive(serverId: Long) {
        deactivateAll()
        activate(serverId)
    }

    suspend fun setActive(server: SubsonicServerDb) {
        setActive(server.id)
    }
}