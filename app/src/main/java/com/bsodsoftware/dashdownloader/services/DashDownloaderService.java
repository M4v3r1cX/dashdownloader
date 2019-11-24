package com.bsodsoftware.dashdownloader.services;

import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class DashDownloaderService {

    private Context _context;
    VideoDownloadService videoDownloadService;

    public DashDownloaderService(Context context) {
        this._context = context;
        this.videoDownloadService = new VideoDownloadService(_context);
    }

    public void process(String tumblrUrl) throws Exception {
        String[] partes = tumblrUrl.split("/");
        String[] forusername = partes[2].split("\\.");

        String username = forusername[0];
        String videoid = partes[4];
        String videoName = username + "_" + videoid + ".mp4";
        String baseurl = "https://www.tumblr.com/video/?1/?2/700/";
        String url = baseurl.replace("?1", username);
        url = url.replace("?2", videoid);

        String html = new HTMLService().execute(url).get();

        if (html != null && !html.isEmpty()) {
            String newUrl = html.substring((html.lastIndexOf("<source src=") + 1), (html.lastIndexOf("type=\"video/mp4\">")));
            if (newUrl != null && !newUrl.isEmpty()) {
                newUrl = newUrl.replace("source src=\"", "");
                newUrl = newUrl.replace("\"", "");

                queueDownload(newUrl, videoName);
            } else {
                throw new Exception("Could find video in " + url);
            }
        } else {
            // Intento 2
            String tumblrServer = "https://ve.media.tumblr.com/tumblr_";    // Base url
            String html2 = new HTMLService().execute(tumblrUrl).get();
            if (html2 != null && !html2.isEmpty()) {
                String searchPhrase = "media.tumblr.com/tumblr_";
                int startIndex = html2.lastIndexOf(searchPhrase);
                int mainIndex = startIndex + searchPhrase.length();
                String videoId = html2.substring(mainIndex, mainIndex + 17);
                if (videoId != null && !videoId.isEmpty()) {
                    String newUrl = tumblrServer + videoId + ".mp4";

                    queueDownload(newUrl, videoName);
                }
            } else {
                throw new Exception("Couldn't retrieve source in " + url);
            }
        }
    }

    private void queueDownload(String url, String videoName) throws ExecutionException, InterruptedException {
        boolean downloaded = videoDownloadService.execute(url, videoName).get();
        String message = "";
        if (downloaded) {
            message = "Video successfuly queued for download. Check your notifications!";
        } else {
            message = "Error downloading video.";
        }

        Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
    }
}
