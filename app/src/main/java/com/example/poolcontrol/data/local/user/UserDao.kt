package com.example.poolcontrol.data.local.user


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(user: UserEntity): Long

    @Update
    suspend fun actualizarUsuario(usuario: UserEntity): Int // Devuelve 1 si tuvo éxito

    @Query("SELECT * FROM users WHERE correo = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Long): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

}