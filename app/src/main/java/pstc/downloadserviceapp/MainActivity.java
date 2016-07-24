package pstc.downloadserviceapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    long idFinal;
    Button downloadButton;
    TextView txt1;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.startDownloadButton);
        txt1 = (TextView) findViewById(R.id.text1);

        txt1.setText("Press Button to start the download.");

        intentFilter = new IntentFilter(DownloadManager
        .ACTION_DOWNLOAD_COMPLETE);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String id = DownloadManager.EXTRA_DOWNLOAD_ID;
                long refId = intent.getLongExtra(id,-1);
                if(refId == idFinal){
                    Toast.makeText(MainActivity.this,"Download Complete",Toast.LENGTH_SHORT).show();
                    txt1.setText("Download finished. Check inside Downloads folder.");

                }
            }
        };

        //registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void StartDownload(View v){
        String str1 = Context.DOWNLOAD_SERVICE;
        DownloadManager downloadManager;
        downloadManager = (DownloadManager) getSystemService(str1);
        Uri uri = Uri.parse("http://www.theminimalist.in/resources/images/portfolio/13/be4fcc5ff32fe99edc92322fa59aacac.jpg");
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setTitle("PSTC logo")
                .setDescription("download in progress..")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"pstc_logo.jpg")
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long downloadRef=downloadManager.enqueue(req);
        idFinal = downloadRef;

        downloadButton.setEnabled(false);
        txt1.setText("Download started! Please wait.");


    }

}
