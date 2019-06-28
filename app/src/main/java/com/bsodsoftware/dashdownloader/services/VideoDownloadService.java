package com.bsodsoftware.dashdownloader.services;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class VideoDownloadService extends AsyncTask<String, String, Boolean> {

    private Context _context;

    public VideoDownloadService(Context context) {
        this._context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean ret = false;
        try {
            String name = params[1];

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(params[0]));
            request.setDescription("DashDownloader is downloading your stuff...");
            request.setTitle("DashDownloader");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

            DownloadManager manager = (DownloadManager) _context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            ret = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
