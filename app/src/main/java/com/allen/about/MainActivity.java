package com.allen.about;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void compartirinfo(View v){
        InputStream stream = null;
        Intent sendIntent = new Intent();
        Uri uri = Uri.parse("android.resource://com.allen.about/drawable/perfil.png");
       
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/png");
        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.mensaje));
        //        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) v.getTag(R.string.app_name));
       sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sendIntent, "Compartir informacion de contacto"));
        }
}
