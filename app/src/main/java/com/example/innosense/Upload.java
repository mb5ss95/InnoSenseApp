package com.example.innosense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;


public class Upload extends AppCompatActivity implements Button.OnClickListener {

    String audio_name;
    String image_name;
    String directory_name;

    Uri audio_path;

    ImageButton btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        init_Title();
        init_wiget();
        init_data();
    }

    public void init_wiget() {
        Button btn1 = findViewById(R.id.choose);
        btn2 = findViewById(R.id.upload);

        btn1.setOnClickListener(Upload.this);
        btn2.setOnClickListener(Upload.this);
    }

    public void init_data() {
        Intent intent = getIntent();

        directory_name = intent.getStringExtra("directory_name");
        image_name = intent.getStringExtra("image_name");


        System.out.println("(upload) Get Directory Name : " + directory_name);
        System.out.println("(upload) Get Chapter : " + image_name);
        //(upload) Get Directory Name : Test World2
        //(upload) Get Id Name : chapter3
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");

                startActivityForResult(Intent.createChooser(intent, "????????? ????????? ???????????????."), 0);
                break;

            case R.id.upload:
                if (audio_path != null) {
                    upload_file(audio_path, audio_name);
                    btn2.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            TextView txt = findViewById(R.id.upload_txt1);
            txt.setText("?????????/page1.mp3");

            directory_name = "?????????";
            audio_name = image_name + ".mp3";

            System.out.println("(upload) Get Audio Name & Audio Path : " + audio_name + ", " + audio_path);
            //(upload) Get Audio Name & Audio Path : null, content://com.android.externalstorage.documents/document/primary%3Arecord.mp3
            btn2.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "????????? ?????????!!", Toast.LENGTH_SHORT).show();

            audio_path = data.getData();
        } else {
            Toast.makeText(getApplicationContext(), "?????? ?????? ?????????.", Toast.LENGTH_SHORT).show();
            btn2.setVisibility(View.INVISIBLE);
        }
    }

    //upload the file
    private void upload_file(final Uri file_path, String file_name) {
        if (file_path != null) {
            System.out.println("test " + file_path + ", " + file_name);
            //test content://com.android.externalstorage.documents/document/primary%3AUpbeat%20Ukulele%20Background%20Music%20-%20That%20Positive%20Feeling%20by%20Alumo.mp3, [test] ???????????????.mp3
            final ProgressDialog progressDialog = new ProgressDialog(Upload.this);
            progressDialog.setTitle("????????? ??? ????????????");
            progressDialog.show();
            //storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(storageRef + "").child(id_name+"/" + file_name);

            FirebaseStorage.getInstance().getReference().child(directory_name +"/"+ file_name).putFile(file_path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                            //btn2.setVisibility(View.INVISIBLE);
                            Upload.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????! ?????? ??? ?????? ???????????????!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }

    }

    private void init_Title() {
        setTitle("  InnoSense");
        ActionBar ab = getSupportActionBar();

        ab.setIcon(R.drawable.ic_baseline_bluetooth_24);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }
}

