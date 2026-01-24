package com.example.poolcontrol.data.local.database // Asegúrate de que este sea el paquete correcto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.poolcontrol.data.local.Piscina.PiscinaEntity
import com.example.poolcontrol.data.local.dao.ReservaPiscinaDao
import com.example.poolcontrol.data.local.disponibilidad.DisponibilidadEntity
import com.example.poolcontrol.data.local.reserva.ReservaEntity
import com.example.poolcontrol.data.local.rol.RolDao
import com.example.poolcontrol.data.local.rol.RolEntity
import com.example.poolcontrol.data.local.user.UserEntity
import com.example.poolcontrol.data.local.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RolEntity::class, UserEntity::class,
                      PiscinaEntity::class, DisponibilidadEntity::class,
                      ReservaEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rolDao(): RolDao
    abstract fun userDao(): UserDao
    abstract fun reservaPiscinaDao(): ReservaPiscinaDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "pool_control.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // IMPORTANTE: Usamos un hilo secundario para insertar los datos iniciales
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                val rDao = database.rolDao()
                                val uDao = database.userDao()
                                val pDao = database.reservaPiscinaDao()

                                // 1. Insertar Roles
                                val roles = listOf(
                                    RolEntity(1, "Administrador"),
                                    RolEntity(2, "Operador"),
                                    RolEntity(3, "Cliente")
                                )
                                roles.forEach { rDao.insertar(it) }

                                // 2. Insertar Usuarios Semilla
                                val users = listOf(
                                    UserEntity(nombre = "Rigoberto", apellido = "Leiva", email = "admin@pool.com", password = "ADMINpool123", numero = "912345678", rolId = 1),
                                    UserEntity(nombre = "Maximiliano", apellido = "Leiva", email = "operador@pool.com", password = "OPerador123", numero = "943218765", rolId = 2),
                                    UserEntity(nombre = "Pedro", apellido = "Campos", email = "cliente@pool.com", password = "CLIente123", numero = "987343834", rolId = 3)
                                )
                                users.forEach { uDao.insertar(it) }

                                val piscinas = listOf(
                                    PiscinaEntity(
                                        id = 1,
                                        nombre = "Piscina Olímpica",
                                        imagenRes = com.example.poolcontrol.R.drawable.piscina1, // Asegúrate que existan en drawable
                                        horarioApertura = "08:00",
                                        horarioCierre = "20:00"
                                    ),
                                    PiscinaEntity(
                                        id = 2,
                                        nombre = "Piscina Recreativa",
                                        imagenRes = com.example.poolcontrol.R.drawable.piscina2,
                                        horarioApertura = "10:00",
                                        horarioCierre = "22:00"
                                    )
                                )
                                piscinas.forEach { pDao.insertarPiscina(it) }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}