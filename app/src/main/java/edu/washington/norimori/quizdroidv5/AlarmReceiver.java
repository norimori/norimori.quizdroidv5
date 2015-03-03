package edu.washington.norimori.quizdroidv5;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.PendingIntent.getActivity;

/**
 * Created by midori on 2/23/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private long enqueue;
    private DownloadManager dm;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        String myHTTPUrl = intent.getStringExtra("URL");
        Toast.makeText(context, "Checking: " + myHTTPUrl, Toast.LENGTH_SHORT).show();
        Log.d("AlarmReceiver", "@AlarmReceiver; Checked for update at: " + myHTTPUrl);

        mContext = context;

        //String myHTTPUrl = "http://tednewardsandbox.site44.com/questions.json";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myHTTPUrl));
        request.setTitle("quizdata.json");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "quizdata.json");
        dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        enqueue = dm.enqueue(request);
        Toast.makeText(mContext.getApplicationContext(), "Questions downloaded from: " + myHTTPUrl, Toast.LENGTH_SHORT).show();
        Cursor c = null;
        DownloadManager.Query query = null;
        query = new DownloadManager.Query();
        if(query!=null) {
            query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|
                    DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
        } else {
            return;
        }
        c = dm.query(query);
        if(c.moveToFirst()) {
            int status = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            Log.d("AlarmReceiver", "" + c.getInt(status));
            if(DownloadManager.STATUS_FAILED == c.getInt(status)) {
                new AlertDialog.Builder(mContext)
                    .setTitle("Download Failed")
                    .setMessage("The download has failed.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Wait for next network check to download file again
                        }
                    })
                    .setNegativeButton("Quit Application", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            }
        }
    }

    /*
    public void writeToFile(String data) {
        try {
            Log.i("AlarmReceiver", "writing downloaded data to file...");

            //Create file with path, name
            File file = new File(getContext().getFilesDir().getAbsolutePath(), "quizdata.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }*/

    public static String getFilePathFromUri(Context c, Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };
            ContentResolver contentResolver = c.getContentResolver();

            Cursor cursor = contentResolver.query(uri, filePathColumn, null,
                    null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else if ("file".equals(uri.getScheme())) {
            filePath = new File(uri.getPath()).getAbsolutePath();
        }
        return filePath;
    }
}
