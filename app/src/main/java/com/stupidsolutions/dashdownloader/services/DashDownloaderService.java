package com.stupidsolutions.dashdownloader.services;

import android.content.Context;
import android.widget.Toast;

public class DashDownloaderService {

    private Context _context;

    public DashDownloaderService(Context context) {
        this._context = context;
    }

    public void process(String tumblrUrl) throws Exception {
        VideoDownloadService videoDownloadService = new VideoDownloadService(_context);

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

                boolean downloaded = videoDownloadService.execute(newUrl, videoName).get();

                String message = "";

                if (downloaded) {
                    message = "Video successfuly downloaded.";
                } else {
                    message = "Error downloading video.";
                }

                Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
            } else {
                throw new Exception("Could find video in " + url);
            }
        } else {
            // Intento 2
            /*String html2 = new HTMLService().execute(tumblrUrl).get();
            if (html2 != null && !html2.isEmpty()) {
                String ss = html2.substring(0, 1100);
                String newUrl = ss.substring((ss.lastIndexOf("<source src=") + 1), (ss.lastIndexOf("type=\"video/mp4\" />")));
                if (newUrl != null && !newUrl.isEmpty()) {

                }
            } else {
                throw new Exception("Couldn't retrieve source in " + url);
            }*/
            throw new Exception("HTML format not yet implemented, I'm working on it! " + url);
        }
    }
}
