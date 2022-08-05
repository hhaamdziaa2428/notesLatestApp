package com.example.navigationdrawer.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.navigationdrawer.database.infoDbSchema.DATABASE_NAME
import com.example.navigationdrawer.database.infoDbSchema.DATABASE_VERSION
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_DATE_OF_BIRTH
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_EMAIL
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_GENDER
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_NAME
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.Columns.KEY_TODO_PASSWORD
import com.example.navigationdrawer.database.infoDbSchema.ToDoTable.TABLE_NAME
import com.example.navigationdrawer.model.ProfileModel

class ToDoDatabaseHandler(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        val createToDoTable = """
  	CREATE TABLE $TABLE_NAME  (
    	$KEY_TODO_NAME  TEXT,
        $KEY_TODO_EMAIL TEXT PRIMARY KEY,
        $KEY_TODO_PASSWORD TEXT,
        $KEY_TODO_DATE_OF_BIRTH TEXT,
        $KEY_TODO_GENDER TEXT
    	);
	"""
        db?.execSQL(createToDoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun  createUser(profileModel: ProfileModel){
       val db: SQLiteDatabase = writableDatabase
       val values = ContentValues()
       values.put(KEY_TODO_NAME, profileModel.name)
       values.put(KEY_TODO_EMAIL,profileModel.email)
       values.put(KEY_TODO_PASSWORD,profileModel.password)
       values.put(KEY_TODO_DATE_OF_BIRTH,profileModel.dateOfBirth)
       values.put(KEY_TODO_GENDER,profileModel.gender)
       db.insert(TABLE_NAME, null, values)
      // db.close()
   }
    fun updateUser(profileModel: ProfileModel): Int{
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO_NAME, profileModel.name)
        values.put(KEY_TODO_EMAIL,profileModel.email)
        values.put(KEY_TODO_PASSWORD,profileModel.password)
        values.put(KEY_TODO_DATE_OF_BIRTH,profileModel.dateOfBirth)
        values.put(KEY_TODO_GENDER,profileModel.gender)

        return db.update(TABLE_NAME, values, "$KEY_TODO_EMAIL=?", arrayOf(profileModel.email))
    }

   @SuppressLint("Range")
    fun readUser(email: String): ArrayList<ProfileModel> {
       val db: SQLiteDatabase = readableDatabase

       val list = ArrayList<ProfileModel>()
       val selectAll = "SELECT * FROM $TABLE_NAME where $KEY_TODO_EMAIL='$email'"

       val cursor: Cursor = db.rawQuery(selectAll, null)

       if (cursor.moveToFirst()) {
           do {
               val nameValue= cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
               val mailValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_EMAIL))
               val passwordValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_PASSWORD))
               val dateValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_DATE_OF_BIRTH))
               val genderValue=cursor.getString(cursor.getColumnIndex(KEY_TODO_GENDER))
               val toDo = ProfileModel(nameValue,mailValue,passwordValue,dateValue,genderValue).apply {

               }

               list.add(toDo)
           } while (cursor.moveToNext())
       }

       cursor.close()

       return list
   }
}


