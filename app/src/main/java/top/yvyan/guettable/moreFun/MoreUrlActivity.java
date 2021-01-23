package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import top.yvyan.guettable.R;

public class MoreUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_url);
    }

    public void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    public void onClick(View view) {
        finish();
    }

    public void resourceCenter(View view) {
        openBrowser(getResources().getString(R.string.url_resource_center));
    }

    public void schoolSong(View view) {
        openBrowser(getResources().getString(R.string.url_school_song));
    }
}