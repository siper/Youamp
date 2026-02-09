package ru.stersh.youamp.core.db

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                """
            CREATE TABLE server_subsonic_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                url TEXT NOT NULL,
                name TEXT NOT NULL,
                username TEXT NOT NULL,
                password TEXT NOT NULL,
                isActive INTEGER NOT NULL,
                authType TEXT NOT NULL DEFAULT 'Token'
            )
            """,
            )
            connection.execSQL(
                """
            INSERT INTO server_subsonic_new (id, url, name, username, password, isActive, authType)
            SELECT id, url, name, username, password, isActive,
                   CASE WHEN useLegacyAuth = 1 THEN 'Unsecure' ELSE 'Token' END
            FROM server_subsonic
            """,
            )
            connection.execSQL("DROP TABLE server_subsonic")
            connection.execSQL("ALTER TABLE server_subsonic_new RENAME TO server_subsonic")
        }
    }
