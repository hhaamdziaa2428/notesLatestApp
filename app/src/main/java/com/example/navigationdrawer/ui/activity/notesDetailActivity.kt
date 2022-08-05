package com.example.navigationdrawer.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawer.R
import de.hdodenhof.circleimageview.CircleImageView

class notesDetailActivity : AppCompatActivity() {
    private var imageview: ImageView? = null
    private var imageviewDelete: ImageView? = null
    private var imageviewShare: ImageView? = null
    private var imageViewFav: ImageView? = null
    private var tag="hide"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_detail)
        imageViewFav=findViewById(R.id.imageFav)
        imageview=findViewById(R.id.imageNotes)
        imageviewDelete=findViewById(R.id.imgDelete)
        imageviewShare=findViewById(R.id.imgShare)


    }
    fun imageClicked(view: View){
        Toast.makeText(this,
            "Menu is selected", Toast.LENGTH_SHORT).show();
        if(tag=="hide")
        {
            imageViewFav?.setVisibility(View.VISIBLE)
            imageviewDelete?.setVisibility(View.VISIBLE)
            imageviewShare?.setVisibility(View.VISIBLE)
            tag="show"

        }
        else
        {
            imageViewFav?.setVisibility(View.INVISIBLE)
            imageviewDelete?.setVisibility(View.INVISIBLE)
            imageviewShare?.setVisibility(View.INVISIBLE)
            tag="hide"
        }

    }


}