package com.biswa1045.studentchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class pdfActivity extends AppCompatActivity {
    private AppCompatSeekBar seekBar;
    private PDFView pdfView;

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        String MY_PDF=getIntent().getStringExtra("NAME");;

        pdfView = findViewById(R.id.pdfView);
        txtView = findViewById(R.id.txtview);
        initSeekBar();
        downloadpdf(MY_PDF);
    }
    @SuppressLint("WrongViewCast")
    private void initSeekBar(){
        seekBar = findViewById(R.id.seekbar);
        seekBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED,PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress *(seekBar.getWidth()-3
                        * seekBar.getThumbOffset())) /seekBar.getMax();
                txtView.setText(""+progress+"%");
                txtView.setX(seekBar.getX() + val + seekBar.getThumbOffset() /2);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private  void downloadpdf(final String fileName){
        new AsyncTask<Void,Integer,Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return downloadpdf();
            }
            @Nullable
            private Boolean downloadpdf(){
                try{
                    File file = getFileStreamPath(fileName);
                    if(file.exists())
                        return true;
                    try{
                        String PDF_LINK = getIntent().getStringExtra("LINK");
                        FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        URL u = new URL(PDF_LINK);
                        URLConnection connection=u.openConnection();
                        int contentLength = connection.getContentLength();
                        InputStream input =new BufferedInputStream(u.openStream());
                        byte data[]= new byte[contentLength];
                        long total = 0;
                        int count;
                        while((count = input.read(data))!= -1){
                            total+=count;
                            publishProgress((int)((total*100) / contentLength));
                            fileOutputStream.write(data,0,count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        input.close();
                        return true;

                    }catch (final Exception e){
                        e.printStackTrace();
                        return false;
                    }



                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                seekBar.setProgress(values[0]);
            }


            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    openPDF(fileName);
                }else{
                    Toast.makeText(pdfActivity.this,"unable to download pdf",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    private void openPDF(String fileName){

        try{
            File file = getFileStreamPath(fileName);
            Log.e("file","file:"+file.getAbsolutePath());
            seekBar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onError((t) -> {
                        Log.e("file:","file:"+t.toString());
                    })
                    .enableAntialiasing(true)
                    .spacing(0)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .load();
        } catch(Exception e){
            e.printStackTrace();
        }




    }


    @Override
    public void onBackPressed() {

            Intent intenteed = new Intent(pdfActivity.this, HomeActivity.class);
            startActivity(intenteed);


    }

}
