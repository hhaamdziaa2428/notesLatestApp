package com.example.navigationdrawer.ui.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.navigationdrawer.R
import com.example.navigationdrawer.database.ToDoDatabaseHandler
import com.example.navigationdrawer.database.userSharedPreference
import com.example.navigationdrawer.database.userSharedPreference.customPreference
import com.example.navigationdrawer.database.userSharedPreference.email
import com.example.navigationdrawer.database.userSharedPreference.userName
import com.example.navigationdrawer.model.ProfileModel
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



class SignupActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "User_data"
    private var PERMISSION_REQUEST_CODE=123
    private var imageview: CircleImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
   var tvSignUp:TextView?=null
    var tvNotesApp:TextView?=null
    var rgGender: RadioGroup?=null
    lateinit var rb: RadioButton
    lateinit var btSignUp: Button
    lateinit var etFullName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var tvDateOfBirth: TextView
    var isFromMainActivity=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        tvSignUp=findViewById(R.id.signUp)
        rgGender = findViewById(R.id.radioGroupGender)
        btSignUp=findViewById(R.id.create)
        etFullName=findViewById(R.id.etName)
        etEmail=findViewById(R.id.etEmailAddress)
        etPassword=findViewById(R.id.etPassword)
        tvDateOfBirth=findViewById(R.id.tvDate)
        tvNotesApp=findViewById(R.id.notesApp)
        isFromMainActivity= intent.getBooleanExtra("isFromMain",false)
        handleUIFlow()
        imageview = findViewById<View>(R.id.imageUser) as CircleImageView
        imageview?.setOnClickListener {

            if(checkPermission()){
                showPictureDialog()
            }
            else {
                requestPermission()
            }
            showPictureDialog() }
        //
        if (isFromMainActivity)
        {
            val prefs = userSharedPreference.customPreference(this, CUSTOM_PREF_NAME)
            val  checkValueMail=prefs.email
            val checkValueName=prefs.userName
            val db = ToDoDatabaseHandler(applicationContext)
            val usersList = checkValueMail?.let { db.readUser(it) }
            if(usersList!=null){
                etFullName.setText(usersList.get(0).name)
                etEmail.setText(usersList.get(0).email)
                etPassword.setText("*******")
                tvDateOfBirth.setText(usersList.get(0).dateOfBirth)
                rgGender?.check(usersList.get(0).gender.toInt())


            }
            tvSignUp?.visibility  =View.GONE
            btSignUp.text="Update"
            if (usersList != null) {
                tvNotesApp!!.text=usersList.get(0).name
            }

        }



    }



    private fun checkPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA )
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false
        }
        return true
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this,
             arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSION_REQUEST_CODE ->
            {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show()

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                  DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                                      fun onClick(dialog:DialogInterface , which:Int) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermission()
                                        }
                                    }
                                })
                        }
                    }
                }
            }

        }
    }

    private fun showMessageOKCancel(message:String , okListener:DialogInterface.OnClickListener ) {
         AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show();
    }
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data?.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@SignupActivity , "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@SignupActivity , "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            imageview!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this@SignupActivity , "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }



    private fun handleUIFlow()
    {
        if (isFromMainActivity)
        {
             tvSignUp?.visibility  =View.GONE
             btSignUp.text="Update"
            tvNotesApp!!.text="Username"
            etEmail.setEnabled(false)
            etEmail.setFocusable(false)
        }
    }

    fun clickOnDateOfBirth(view: View) {
        val calendar=Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this@SignupActivity, DatePickerDialog.OnDateSetListener
        { view, year, monthOfYear, dayOfMonth ->

            tvDateOfBirth.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)

        }, year, month, day)

        datePickerDialog.show()
    }
    private fun checkName(): Boolean {
        val name=etFullName.text.toString()
        if (name.isEmpty()|| name.length<3)
        {
            etFullName.error="Enter your name"
            return false
        }
        return true
    }
    private fun checkEmail(): Boolean {
        val mail=etEmail.text.toString()
        if (!mail.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
              return true
        }
        else {
            etEmail.error="Enter correct email"
            return false
        }
    }
    private fun checkPassword(): Boolean {
        val cpassword=etPassword.text.toString()
        if (cpassword.isEmpty()){
            etPassword.error="Password Required"
            return false
        }
        return true
    }





    fun buttonClick(view: View) {
        //  mail , password and name
         val checkName=checkName()
         val checkEmail=checkEmail()
         val checkPassword=checkPassword()
        //    Button after creating
        Toast.makeText(baseContext, btSignUp.text, Toast.LENGTH_SHORT).show()


       // for DB

        // checking mail , password and name
       if( checkName && checkEmail  && checkPassword) {


           if (isFromMainActivity) {
               updateProfile()
               finish()

           } else {
               // shared preferences
               checkEmailExist()
               storePreference()
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
               finish()

           }
       }
        else {
            Toast.makeText(baseContext,"Need to update it",Toast.LENGTH_SHORT).show()
        }


    }


    fun updateProfile(){
        if(isFromMainActivity){
            val db = ToDoDatabaseHandler(applicationContext)
            db.updateUser(getCurrentUserModel())
        }

    }
    private fun checkEmailExist():Boolean{
        val db = ToDoDatabaseHandler(applicationContext)
        val usersList = db.readUser(etEmail.text.toString())
        if (usersList == null || usersList.isEmpty())
        {
            db.createUser(getCurrentUserModel())
            return false
        }
        return true
    }
    private fun getCurrentUserModel():ProfileModel
    {
     return ProfileModel(etFullName.text.toString(),etEmail.text.toString(),etPassword.text.toString(),tvDateOfBirth.text.toString(),rgGender?.checkedRadioButtonId.toString())
    }
    fun storePreference(){
        val prefs = customPreference(this, CUSTOM_PREF_NAME)

        prefs.userName=etFullName.text.toString()
        prefs.email= etEmail.text.toString()
    }


}


