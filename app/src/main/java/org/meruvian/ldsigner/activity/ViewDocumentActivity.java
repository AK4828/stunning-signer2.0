package org.meruvian.ldsigner.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.pdfview.PDFView;
import org.meruvian.ldsigner.LdsignerApplication;
import org.meruvian.ldsigner.R;
import org.meruvian.ldsigner.entity.Document;
import org.meruvian.ldsigner.entity.DocumentDao;
import org.meruvian.ldsigner.entity.FileInfo;
import org.meruvian.ldsigner.entity.FileInfoDao;
import org.meruvian.ldsigner.job.DocumentSignJob;
import com.path.android.jobqueue.JobManager;

import java.io.File;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by akm on 07/06/16.
 */
public class ViewDocumentActivity extends AppCompatActivity {

    @Bind(R.id.fab_sign) FloatingActionButton fabSign;
    @Bind(R.id.pdfview) PDFView pdfView;
    private AlertDialog alertDialog;
    private JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_document_activity);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fabSign.getLayoutParams();
            p.setMargins(0, 0, dpToPx(this, 8), 0); // get rid of margins since shadow area is now the margin
            fabSign.setLayoutParams(p);
        }

        fabSign.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_paint_brush).colorRes(android.R.color.white));
    }

    @OnClick(R.id.fab_sign)
    public void onClick() {
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String filePath = getIntent().getStringExtra("filePath");
        if (filePath != null) {

            pdfView.fromFile(new File(filePath))
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();

            setupAlertDialog();
        } else {
            Toast.makeText(this, "Choose file first", Toast.LENGTH_LONG);
            Intent i = new Intent(ViewDocumentActivity.this, SignedDocumentActivity.class);
            startActivity(i);

        }

    }

    private void setupAlertDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        DialogInterface.OnClickListener onPositiveButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LdsignerApplication app  = LdsignerApplication.getInstance();
                DocumentDao documentDao = app.getDaoSession().getDocumentDao();
                FileInfoDao fileInfoDao = app.getDaoSession().getFileInfoDao();
                String filePath = getIntent().getStringExtra("filePath");

                Document document = new Document();

                FileInfo fileInfo = document.getDetachedFileInfo();
                fileInfo.setPath(filePath);
                long fileInfoId = fileInfoDao.insert(fileInfo);

                document.setDbCreateDate(new Date());
                document.setDbActiveFlag(1);
                document.setFileInfoId(fileInfoId);

                documentDao.insert(document);

                DocumentSignJob.newInstance(document.getDbId(), input.getText().toString(), filePath);

                jobManager = LdsignerApplication.getInstance().getJobManager();
                jobManager.addJobInBackground(DocumentSignJob.newInstance(document.getDbId(), input.getText().toString(), filePath));
            }
        };

        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.password)
                .setPositiveButton(R.string.sign, onPositiveButton)
                .setCancelable(false)
                .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_key))
                .setView(input)
                .create();

    }

    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
}
