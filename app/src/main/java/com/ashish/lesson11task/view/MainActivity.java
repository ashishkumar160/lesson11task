package com.ashish.lesson11task.view;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ashish.lesson11task.DownloadImageIntentService;
import com.ashish.lesson11task.ImageDownloadTask;
import com.ashish.lesson11task.R;
import com.ashish.lesson11task.utils.DirectoryFileObserver;
import com.ashish.lesson11task.utils.NetworkUtils;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ListItemClickListener {

    private EnterURLDialog urlDialog;
    private List<File> mFiles;
    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private DirectoryFileObserver directoryFileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "lesson11taskimages");

        mFiles = new ArrayList<>();
        if (!folder.exists()) {
            folder.mkdir();
        }
        Collections.addAll(mFiles, folder.listFiles());

        directoryFileObserver = new DirectoryFileObserver(folder.getAbsolutePath(), new ImageAdapter.ListItemUpdateListener() {
            @Override
            public void onListUpdate(File file) {
                mFiles.add(file);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        directoryFileObserver.startWatching();

        mAdapter = new ImageAdapter(mFiles, this);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        directoryFileObserver.stopWatching();
    }

    public void fabClicked(View view) {
        urlDialog = new EnterURLDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("listener", new EnterURLDialog.EnterURLDialogListener() {
            @Override
            public void onDialogPositiveClick(AppCompatDialogFragment dialog, String urlString, String nameString) {
                if (nameString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a name for the image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    URL url = NetworkUtils.buildUrl(urlString);
                    Intent downloadImageIntent = new Intent(getApplicationContext(), DownloadImageIntentService.class);
                    downloadImageIntent.putExtra("imageUrl", url);
                    downloadImageIntent.putExtra("imageName", nameString);
                    downloadImageIntent.setAction(ImageDownloadTask.ACTION_DOWNLOAD_IMAGE);
                    startService(downloadImageIntent);
                } catch (MalformedURLException e) {
                    Toast.makeText(MainActivity.this, "The URL is not valid.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        urlDialog.setArguments(bundle);
        urlDialog.show(getSupportFragmentManager(), "urlDialog");
    }

    @Override
    public void onListItemClick(File file) {
        Intent intent = new Intent(this, FullscreenActivity.class);
        intent.putExtra("imageFile", file);
        startActivity(intent);
    }

}
