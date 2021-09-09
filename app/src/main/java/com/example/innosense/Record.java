package com.example.innosense;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Record extends AppCompatActivity implements Button.OnTouchListener {
    StorageReference storageRef;

    MediaRecorder recorder;

    String image_name;
    String RECORDED_FILE;

    ImageButton btn1, btn2, btn3;
    ImageView image_view;
    Switch check;
    Chronometer ch;
    TextView txt;

    Animation start_rotate, stop_rotate, show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        File file = new File(Environment.getExternalStorageDirectory(), "record.mp3");
        RECORDED_FILE = file.getAbsolutePath();

        init_Anim();
        init_Wiget();
        init_Title();
        init_Path();
    }

    private void init_Anim() {
        start_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        stop_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stop);
    }


    private void init_recorder() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(RECORDED_FILE);
        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception ex) {
            Log.e("Record", "Exception :", ex);
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder = null;
        }
    }

    public void init_Wiget() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        check = findViewById(R.id.check);
        ch = findViewById(R.id.ch);
        txt = findViewById(R.id.txt1);
        image_view = findViewById(R.id.img_view);

        btn1.setOnTouchListener(Record.this);
        btn2.setOnTouchListener(Record.this);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked()) {
                    image_view.setVisibility(View.INVISIBLE);
                    txt.setVisibility(View.VISIBLE);
                } else {
                    image_view.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void init_Path() {

        image_name = "page1";

        System.out.println("(Record) Get Image Name: " + image_name);
        //(Record) Get Directory Name : story1
        //(Record) Get Storage Reference : gs://project-83e1e.appspot.com/story1

        init_image("사진/"+image_name+".jpg");
        init_text("스토리/"+image_name+".txt");
        //init_text(temp);
    }

    private void init_image(String temp) {
        //  directory_name/file_name

        FirebaseStorage.getInstance().getReference().child(temp).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(Record.this)
                            .load(task.getResult())
                            .into(image_view);
                    image_view.setVisibility(View.VISIBLE);
                    btn1.setClickable(true);

                    ProgressBar progressBar = findViewById(R.id.progress);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    //Toast.makeText(mp3.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(Record.this, "이미지를 불러올 수 없습니다. 잠시 후 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void init_text(String temp) {
        //long ONE_MEGABYTE = 1024 * 1024;
        System.out.println("(Show) Get Image Name & Audio Name22222222222222222222222222222 : " + temp);
        FirebaseStorage.getInstance().getReference().child(temp).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                txt.setText(new String(bytes));

                ProgressBar progressBar = findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Record.this, "텍스트를 불러올 수 없습니다. 잠시 후 다시 시도하세요.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init_Title() {
        setTitle("  InnoSense");
        ActionBar ab = getSupportActionBar();

        ab.setIcon(R.drawable.ic_baseline_bluetooth_24);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    
    private void set_wiget_state1() {
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.VISIBLE);

        btn3.setImageResource(R.drawable.ring);
        btn3.startAnimation(start_rotate);
    }

    private void set_wiget_state2() {
        btn2.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.VISIBLE);

        btn3.clearAnimation();
        btn3.setImageResource(R.drawable.eyes);
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.btn1:
                    //init_recorder();
                    ch.setBase(SystemClock.elapsedRealtime());
                    ch.setAnimation(show);
                    ch.setVisibility(View.VISIBLE);
                    ch.start();

                    set_wiget_state1();
                    break;

                case R.id.btn2:
                    //stopRecording();
                    ch.setVisibility(View.INVISIBLE);
                    ch.stop();
                    //upload_file(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "[test] test.mp3");
                    set_wiget_state2();
                    Toast.makeText(Record.this, "InnoSense/"+image_name+".mp3로 저장 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return false;
    }
}

