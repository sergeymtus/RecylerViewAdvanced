package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.Companion.REPLACE
import ru.netology.nmedia.entity.PostRemoteKeyEntity

@Dao
interface PostRemoteKeyDao {
    @Query("SELECT COUNT(*) == 0 FROM PostRemoteKeyEntity")
   suspend fun isEmpty() : Boolean

   @Query("SELECT MAX(id) FROM PostRemoteKeyEntity")
    suspend fun max(): Long?

    @Query("SELECT MIN(id) FROM PostRemoteKeyEntity")
    suspend fun min(): Long?

    @Insert(onConflict = REPLACE)
    suspend fun insert(key: PostRemoteKeyEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(keys: List<PostRemoteKeyEntity>)

    @Query("DELETE FROM PostRemoteKeyEntity ")
    suspend fun removeAll()

}