package com.example.photosearchapp.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AsyncTask<String,Integer,Long> {
    ProgressDialog mProgressDialog;
    Context context;
    Bitmap bitmap;

    public DownloadFile(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    @Override
    protected Long doInBackground(String... aurl) {
        int count=0;

        try {

            URL url = new URL((String) aurl[0]);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            String targetFileName = aurl[0].substring(aurl[0].lastIndexOf('/')+1,aurl[0].lastIndexOf('.'))+".jpg";//Change name and subname
            int lenghtOfFile = urlConnection.getContentLength();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            File folder = new File(SDCardRoot,"/photosearch/");
            if (!folder.exists()) {
                folder.mkdir();//If there is no folder it will be created.
            }
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(new File(folder.getPath()+"/"+targetFileName));
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) >0) {
                total += count;
                publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
        if(aurl[0].contains("farm")){
            saveImageFile(bitmap,aurl[0]);
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        mProgressDialog.setProgress(progress[0]);
        if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
            mProgressDialog.dismiss();
            Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onPostExecute(String result) {
    }
    public void saveImageFile(Bitmap bitmap,String url) {
        FileOutputStream out = null;

        try {
            out = getoutputstream(url);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private FileOutputStream getoutputstream(String url) throws FileNotFoundException {
        String targetFileName = url.substring(url.lastIndexOf('/')+1,url.lastIndexOf('.'))+".jpg";
        File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();

        File folder = new File(SDCardRoot,"/photosearch/");
        if (!folder.exists()) {
            folder.mkdir();//If there is no folder it will be created.
        }
        FileOutputStream output = new FileOutputStream(new File(folder.getPath()+"/"+targetFileName));
        return output;
    }
}
