package com.far_sstrwnt.cinemania.shared.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.far_sstrwnt.cinemania.model.MediaEntity

@Dao
interface MediaDao {

    @Query("SELECT * FROM favorite_media")
    suspend fun getMediaFavorite(): List<MediaEntity>

    @Query("SELECT * FROM favorite_media WHERE id = :id")
    suspend fun getMediaFavoriteById(id: String): MediaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaFavorite(media: MediaEntity)

    @Query("DELETE FROM favorite_media WHERE id = :id")
    suspend fun deleteMediaFavoriteById(id: String): Int

    @Query("DELETE FROM favorite_media")
    suspend fun deleteMediaFavorite()
}