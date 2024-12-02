package data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import data.model.Userdata

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Userdata)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): Userdata?
}
