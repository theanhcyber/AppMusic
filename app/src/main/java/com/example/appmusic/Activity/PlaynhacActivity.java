package com.example.appmusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.appmusic.Adapter.ViewPaperPlayListNhac;
import com.example.appmusic.Fragment.Fragment_Dia_Nhac;
import com.example.appmusic.Fragment.Fragment_Play_Danh_Sach_Bai_Hat;
import com.example.appmusic.Model.Songs;
import com.example.appmusic.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlaynhacActivity extends AppCompatActivity {

    Toolbar toolbarplaynhac;
    TextView txtTimesong,txtTotaltimesong;
    SeekBar sktime;
    ImageButton imgplay,imgrepeat,imgnext,imgpre,imgrandom;
    ViewPager viewPagerplaynahc;
    public static ArrayList<Songs> mangbaihat = new ArrayList<>();
    public static ViewPaperPlayListNhac adapternhac;
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_Danh_Sach_Bai_Hat fragment_play_danh_sach_bai_hat;
    MediaPlayer mediaPlayer;
    int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playnhac);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDataFromIntent();
        init();
        eventClick();

    }

    private void eventClick() {
        Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
                if(adapternhac.getItem(1)!= null){
                    if(mangbaihat.size() > 0) {
                        fragment_dia_nhac.Playnhac(mangbaihat.get(0).getImageSong());
                        handler.removeCallbacks(this);

                    }else {
                        handler.postDelayed(this,300);
                    }
                }
           }
       },500);
       imgplay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mediaPlayer.isPlaying()){
                   mediaPlayer.pause();
                   imgplay.setImageResource(R.drawable.iconplay);
               }else {
                   mediaPlayer.start();
                   imgplay.setImageResource(R.drawable.iconpause);
               }
           }
       });
       imgrepeat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(repeat == false){
                   if(checkrandom == true){
                       checkrandom = false;
                       imgrepeat.setImageResource(R.drawable.iconsyned);
                       imgrandom.setImageResource(R.drawable.iconsuffle);
                   }
                   imgrepeat.setImageResource(R.drawable.iconsyned);
                   repeat = true;
               }else {
                   imgrepeat.setImageResource(R.drawable.iconrepeat);
                   repeat = false;
               }
           }
       });
       imgrandom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(checkrandom == false){
                   if(repeat == true){
                       repeat = false;
                       imgrandom.setImageResource(R.drawable.iconshuffled);
                       imgrepeat.setImageResource(R.drawable.iconrepeat);
                   }
                   imgrandom.setImageResource(R.drawable.iconshuffled);
                   checkrandom = true;
               }else {
                   imgrandom.setImageResource(R.drawable.iconsuffle);
                   checkrandom = false;
               }
           }
       });
       sktime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               mediaPlayer.seekTo(seekBar.getProgress());
           }
       });
       imgnext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mangbaihat.size() >0){
                   if(mediaPlayer.isPlaying() || mediaPlayer != null){
                       mediaPlayer.stop();
                       mediaPlayer.release();
                       mediaPlayer = null;
                   }
                   if(position < (mangbaihat.size())){
                       imgplay.setImageResource(R.drawable.iconpause);
                       position++;
                       if(repeat == true){
                           if(position == 0){
                               position = mangbaihat.size();
                           }
                           position -= 1;
                       }
                       if(checkrandom == true){
                           Random random = new Random();
                           int index = random.nextInt(mangbaihat.size());
                           if(index == position){
                               position = index -1;
                           }
                           position = index;
                       }
                       if(position > (mangbaihat.size() - 1)){
                           position = 0;
                       }
                       new PlayMP3().execute(mangbaihat.get(position).getLinkSong());
                       fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImageSong());
                       getSupportActionBar().setTitle(mangbaihat.get(position).getNameSong());
                       UpdateTime();
                   }
               }
               imgpre.setClickable(false);
               imgnext.setClickable(false);
               Handler handler1 = new Handler();
               handler1.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       imgpre.setClickable(true);
                       imgnext.setClickable(true);
                   }
               },5000);
           }
       });
       imgpre.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mangbaihat.size() >0){
                   if(mediaPlayer.isPlaying() || mediaPlayer != null){
                       mediaPlayer.stop();
                       mediaPlayer.release();
                       mediaPlayer = null;
                   }
                   if(position < (mangbaihat.size())){
                       imgplay.setImageResource(R.drawable.iconpause);
                       position--;

                       if(position < 0){
                           position = mangbaihat.size()-1;
                       }
                       if(repeat == true){

                           position += 1;
                       }
                       if(checkrandom == true){
                           Random random = new Random();
                           int index = random.nextInt(mangbaihat.size());
                           if(index == position){
                               position = index -1;
                           }
                           position = index;
                       }

                       new PlayMP3().execute(mangbaihat.get(position).getLinkSong());
                       fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImageSong());
                       getSupportActionBar().setTitle(mangbaihat.get(position).getNameSong());
                       UpdateTime();
                   }
               }
               imgpre.setClickable(false);
               imgnext.setClickable(false);
               Handler handler1 = new Handler();
               handler1.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       imgpre.setClickable(true);
                       imgnext.setClickable(true);
                   }
               },5000);
           }
       });
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if(intent != null){
            if(intent.hasExtra("cakhuc")){
                Songs songs = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(songs);
            }
            if(intent.hasExtra("cacbaihat")){
                ArrayList<Songs> baihatArraylist = intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat = baihatArraylist;
            }
        }
    }

    private void init() {
        toolbarplaynhac = findViewById(R.id.toolbarplaymusic);
        txtTimesong = findViewById(R.id.textviewtimesong);
        txtTotaltimesong = findViewById(R.id.textviewtotaltimesong);
        sktime = findViewById(R.id.seekbarsong);
        imgrepeat = findViewById(R.id.imgbuttonrepeat);
        imgplay = findViewById(R.id.imgbuttonplay);
        imgnext = findViewById(R.id.imgbuttonnext);
        imgrandom = findViewById(R.id.imgbuttonsuffle);
        imgpre = findViewById(R.id.imgbuttonpre);
        viewPagerplaynahc = findViewById(R.id.viewpaperplaynhac);
        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarplaynhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                mangbaihat.clear();
            }
        });
        toolbarplaynhac.setTitleTextColor(Color.WHITE);
        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_play_danh_sach_bai_hat = new Fragment_Play_Danh_Sach_Bai_Hat();
        adapternhac = new ViewPaperPlayListNhac(getSupportFragmentManager());
        adapternhac.AddFragment(fragment_play_danh_sach_bai_hat);
        adapternhac.AddFragment(fragment_dia_nhac);
        viewPagerplaynahc.setAdapter(adapternhac);
        fragment_dia_nhac = (Fragment_Dia_Nhac) adapternhac.getItem(1);
        if(mangbaihat.size()>0){
            getSupportActionBar().setTitle(mangbaihat.get(0).getNameSong());
            new PlayMP3().execute(mangbaihat.get(0).getLinkSong());
            imgplay.setImageResource(R.drawable.iconpause);
        }
    }

    class PlayMP3 extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });
            mediaPlayer.setDataSource(baihat);
            mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mediaPlayer.start();
            TimeSong();
            UpdateTime();
        }
    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        sktime.setMax(mediaPlayer.getDuration());
    }
    private void UpdateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    sktime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    txtTimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
                }
            }
        },300);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next == true){
                    if(position < (mangbaihat.size())){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position--;

                        if(position < 0){
                            position = mangbaihat.size()-1;
                        }
                        if(repeat == true){

                            position += 1;
                        }
                        if(checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if(index == position){
                                position = index -1;
                            }
                            position = index;
                        }

                        new PlayMP3().execute(mangbaihat.get(position).getLinkSong());
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImageSong());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getNameSong());

                }
                imgpre.setClickable(false);
                imgnext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpre.setClickable(true);
                        imgnext.setClickable(true);
                    }
                },5000);
                next = false;
                handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }
}