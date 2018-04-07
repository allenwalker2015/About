package com.allen.about;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/perfil.png";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       checkProfileFile();
       checkPermisionsSD();
    }

    void checkProfileFile(){
        File file=new File(path);
        if(!file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.perfil);
            OutputStream out = null;


            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path = file.getPath();
        }
    }
    public void checkPermisionsSD(){
        if (ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    public void compartirinfo(View v){
        Uri bmpUri = Uri.parse("file://"+path);
        Intent shareIntent;
        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.mensaje));
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent,"Share with"));

































//        InputStream stream = null;
//        Intent sendIntent = new Intent();
//        Uri uri = Uri.parse("android.resource://com.allen.about/drawable/perfil.png");
//
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setType("image/png");
//        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);

//        //        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) v.getTag(R.string.app_name));
//       sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(Intent.createChooser(sendIntent, "Compartir informacion de contacto"));
        }
}
