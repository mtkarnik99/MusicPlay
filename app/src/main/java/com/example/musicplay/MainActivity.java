package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView lvMusic;
    Button btnStop,btnPause;
    ImageButton ibtnRev,ibtnFwd;
    String name[],path[];
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMusic = findViewById(R.id.lvMusic);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        ibtnFwd = findViewById(R.id.ibtnFwd);
        ibtnRev = findViewById(R.id.ibtnRev);
        mp = new MediaPlayer();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,null);
        name = new String[cursor.getCount()];
        path = new String[cursor.getCount()];

        int i = 0 ;
        while(cursor.moveToNext())
        {
            name[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            path[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            i++;
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,name);
        lvMusic.setAdapter(arrayAdapter);

        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String p = path[position];
                    mp.reset();
                    mp.setDataSource(p);
                    mp.prepare();
                    mp.start();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying())
                    mp.stop();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying())
                {
                    mp.pause();
                    btnPause.setText("Resume");
                }
                else
                {
                    mp.start();
                    btnPause.setText("Pause");
                }
            }
        });

        ibtnRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.seekTo(mp.getCurrentPosition()-3000);
            }
        });

        ibtnFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.seekTo(mp.getCurrentPosition()+3000);
            }
        });
    }
}
