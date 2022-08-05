package com.example.navigationdrawer.database

object notesDbSchema {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "notesStore.db"
    object NotesDataTable {

        const val TABLE_NAME = "notesData"
        object Columns {
            const val KEY_ID="id"
            const val USER_MAIL="useremail"
            const val KEY_NOTES_NAME = "Name"
            const val KEY_TODO_DESCRIPTION="toDescription"
            const val KEY_TODO_DATE="todoDate"
            const val KEY_FAVOURITE="isFav"
        }

    }


}