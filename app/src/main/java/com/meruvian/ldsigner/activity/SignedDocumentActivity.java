package com.meruvian.ldsigner.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.meruvian.ldsigner.LdsignerApplication;
import com.meruvian.ldsigner.R;
import com.meruvian.ldsigner.adapter.DocumentListAdapter;
import com.meruvian.ldsigner.entity.DaoSession;
import com.meruvian.ldsigner.entity.Document;
import com.meruvian.ldsigner.entity.DocumentDao;
import com.meruvian.ldsigner.entity.KeyStore;
import com.meruvian.ldsigner.entity.SignedDocument;
import com.meruvian.ldsigner.job.DocumentSignJob;
import com.meruvian.ldsigner.job.JobStatus;
import com.meruvian.ldsigner.utils.AuthenticationUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by akm on 06/06/16.
 */
public class SignedDocumentActivity extends AppCompatActivity {

    @Bind(R.id.fab_add) FloatingActionButton fab;
    @Bind(R.id.doc_list) RecyclerView docList;
    @Bind(R.id.toolbar) Toolbar toolbar;
    private DocumentListAdapter docListAdapter;
    protected static final int CHOOSE_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_document_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
            p.setMargins(0, 0, dpToPx(this, 8), 0); // get rid of margins since shadow area is now the margin
            fab.setLayoutParams(p);
        }

        fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_plus).colorRes(android.R.color.white));
        docList.setHasFixedSize(true);
        docList.setLayoutManager(new LinearLayoutManager(this));
        docList.setAdapter(docListAdapter = new DocumentListAdapter(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDocuments();
    }

    @OnClick(R.id.fab_add)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Browse file"), CHOOSE_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SignedDocumentActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEventMainThread(DocumentSignJob.DocumentSignEvent event) {
        if (event.getStatus() == JobStatus.SUCCESS) {
            Toast.makeText(this, "Document Signed.", Toast.LENGTH_SHORT).show();
        } else if (event.getStatus() == JobStatus.USER_ERROR){
            Toast.makeText(this, "Signing failed, wrong password or corrupted file.", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadDocuments() {
        DaoSession daoSession = LdsignerApplication.getInstance().getDaoSession();
        final DocumentDao docDao = daoSession.getDocumentDao();
        new AsyncTask<Void, Void, List<Document>>() {
            @Override
            protected List<Document> doInBackground(Void... params) {
                return docDao.queryBuilder()
                        .orderDesc(DocumentDao.Properties.DbCreateDate)
                        .limit(50)
                        .build().forCurrentThread()
                        .list();
            }

            @Override
            protected void onPostExecute(List<Document> documents) {
                docListAdapter.addDocuments(documents);
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_FILE) {
            fileChoosed(data == null ? null : data.getData());

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fileChoosed(Uri data) {
        if (data == null) {
        } else {
            String path = data.getPath();
            int ksType = -1;
            Log.d("Uri", path);

            Intent intent = new Intent(SignedDocumentActivity.this, ViewDocumentActivity.class);
            intent.putExtra("filePath", path);
            startActivity(intent);
        }
    }

    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
}
