package com.example.android.imagedownload;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private EditText editText;
    private Button button;
    String imagesLinks[];

    private ProgressBar progressBar;
    LinearLayout loadingSecton=null;
    ListView listView;
    LinearLayout loading_section=null;

    Thread thread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText= (EditText) findViewById(R.id.editText);
        button= (Button) findViewById(R.id.download_button);
        imagesLinks=getResources().getStringArray(R.array.imageUrls);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);


        listView= (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);

        loading_section= (LinearLayout) findViewById(R.id.loading_section);

        handler=new Handler();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        editText.setText(imagesLinks[i]);



        TextView temp= (TextView) view;
        Toast.makeText(this,temp.getText()+" "+i,Toast.LENGTH_SHORT).show();

    }

    public void download(View view){

        String url=editText.getText().toString();
        Thread myThread=new Thread(new DownloadImageTheread(url));
        myThread.start();



    }

    public boolean downloadImageWithUrl(String url){

        boolean successfull=false;
        URL downloadUrl=null;
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        FileOutputStream fileOutputStream=null;
        File file=null;
        try {
            downloadUrl=new URL(url);

            httpURLConnection= (HttpURLConnection) downloadUrl.openConnection();
            inputStream=httpURLConnection.getInputStream();

            file= new File(this.getFilesDir(),Uri.parse(url).getLastPathSegment());

            fileOutputStream=new FileOutputStream(file);

            int read=-1;
            byte buffer[]=new byte[1024];

            while ((read=inputStream.read(buffer))!=-1){


                fileOutputStream.write(buffer,0,read);
                L.m(" "+read);

            }
            successfull=true;

        } catch (MalformedURLException e) {

            L.m(" "+e);

        } catch (IOException e) {

            L.m(" "+e);

        }
        finally {


            //thi
            //look can be done by runOnUIThread()
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loading_section.setVisibility(View.GONE);
                }
            });
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {

                    L.m(" "+e);

                }
            }
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    L.m(" "+e);
                }
            }

        }
        return successfull;

    }

    private class DownloadImageTheread implements Runnable{

        private String url;
        DownloadImageTheread(String url){
            this.url=url;
        }
        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    loading_section.setVisibility(View.VISIBLE);
                }
            });

            downloadImageWithUrl(url);
        }
    }

}
