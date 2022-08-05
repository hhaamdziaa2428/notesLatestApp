package com.example.navigationdrawer.database

object infoDbSchema {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "notes.db"
    object ToDoTable {

        const val TABLE_NAME = "profile"
        object Columns {

            const val KEY_TODO_NAME = "todoName"

            const val KEY_TODO_EMAIL="toEmail"

            const val KEY_TODO_PASSWORD="password"

            const val KEY_TODO_DATE_OF_BIRTH="todoDateOfBirth"

            const val KEY_TODO_GENDER="todoGender"

        }

    }


}