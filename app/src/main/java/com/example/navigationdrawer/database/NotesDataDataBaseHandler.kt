package com.example.navigationdrawer.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_EMAIL
import com.example.navigationdrawer.database.notesDbSchema.DATABASE_NAME
import com.example.navigationdrawer.database.notesDbSchema.DATABASE_VERSION
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.KEY_FAVOURITE
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.KEY_ID
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.KEY_NOTES_NAME
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.KEY_TODO_DATE
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.KEY_TODO_DESCRIPTION
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.Columns.USER_MAIL
import com.example.navigationdrawer.database.notesDbSchema.NotesDataTable.TABLE_NAME
import com.example.navigationdrawer.model.NotesModel

class NotesDataDataBaseHandler (private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        val createToDoTable = """
  	CREATE TABLE $TABLE_NAME  (
        $KEY_ID Int PRIMARY KEY,
        $USER_MAIL TEXT,
    	$KEY_NOTES_NAME TEXT,
        $KEY_TODO_DESCRIPTION TEXT ,
        $KEY_TODO_DATE TEXT,
        $KEY_FAVOURITE BOOLEAN
    	);
	"""
        db?.execSQL(createToDoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun  createUser(notesModel: NotesModel){
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_ID,notesModel.id)
        values.put(USER_MAIL,notesModel.emailId)
        values.put(KEY_NOTES_NAME,notesModel.name)
        values.put(KEY_TODO_DESCRIPTION,notesModel.description)
        values.put(KEY_TODO_DATE,notesModel.date)
        values.put(KEY_FAVOURITE,notesModel.isFav)

        db.insert(TABLE_NAME, null, values)
        // db.close()
    }
    fun updateUser(notesModel: NotesModel): Int{
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_ID,notesModel.id)
        values.put(USER_MAIL,notesModel.emailId)
        values.put(KEY_NOTES_NAME, notesModel.name)
        values.put(KEY_TODO_DESCRIPTION,notesModel.description)
        values.put(KEY_TODO_DATE,notesModel.date)
        values.put(KEY_FAVOURITE,notesModel.isFav)

        return db.update(TABLE_NAME, values, "$KEY_TODO_DESCRIPTION=?", arrayOf(notesModel.description))
    }

    @SuppressLint("Range")
    fun readUser(id: Int): ArrayList<NotesModel> {
        val db: SQLiteDatabase = readableDatabase

        val list = ArrayList<NotesModel>()
        val selectAll = "SELECT * FROM $TABLE_NAME where $KEY_ID=''$id''"

        val cursor: Cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                val idValue=cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val mailValue=cursor.getString(cursor.getColumnIndex(USER_MAIL))
                val nameValue= cursor.getString(cursor.getColumnIndex(KEY_NOTES_NAME))
                val descriptionValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_DESCRIPTION))
                val dateValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_DATE))
                val favValue=cursor.getInt(cursor.getColumnIndex(KEY_FAVOURITE))>0
                val toDo = NotesModel(idValue,mailValue,nameValue, descriptionValue,dateValue,favValue).apply {

                }

                list.add(toDo)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }
    @SuppressLint("Range")
    fun readUserAll(): ArrayList<NotesModel> {
        val db: SQLiteDatabase = readableDatabase

        val list = ArrayList<NotesModel>()
        val selectAll = "SELECT * FROM $TABLE_NAME "

        val cursor: Cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                val idValue=cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val mailValue=cursor.getString(cursor.getColumnIndex(USER_MAIL))
                val nameValue= cursor.getString(cursor.getColumnIndex(KEY_NOTES_NAME))
                val descriptionValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_DESCRIPTION))
                val dateValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_DATE))
                val favValue=cursor.getInt(cursor.getColumnIndex(KEY_FAVOURITE))>0
                val toDo = NotesModel(idValue,mailValue,nameValue, descriptionValue,dateValue,favValue).apply {

                }

                list.add(toDo)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }
}