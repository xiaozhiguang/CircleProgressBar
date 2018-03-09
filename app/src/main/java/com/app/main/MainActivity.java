package com.app.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.views.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar cpbDownloadPause;
    private CircleProgressBar cpbDownloadStatus;
    private CircleProgressBar cpbDownloadFinish;
    private CircleProgressBar cpbDownloadDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cpbDownloadPause = findViewById(R.id.cpb_download_pause);
        cpbDownloadStatus = findViewById(R.id.cpb_download_status);
        cpbDownloadFinish = findViewById(R.id.cpb_download_finish);
        cpbDownloadDefault = findViewById(R.id.cpb_download_default);

        cpbDownloadDefault.setStatue(CircleProgressBar.DOWNLOAD_DEF);
        cpbDownloadPause.setStatue(CircleProgressBar.DOWNLOAD_PAUSE);
        cpbDownloadStatus.setStatue(CircleProgressBar.DOWNLOAD_STATUE);
        cpbDownloadFinish.setStatue(CircleProgressBar.DOWNLOAD_FINISH);

        cpbDownloadPause.setProgress(30);

        cpbDownloadStatus.setProgress(60);

    }
}
