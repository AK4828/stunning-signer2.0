package org.meruvian.ldsigner.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.pdfview.PDFView;
import com.meruvian.ldsigner.R;
import com.path.android.jobqueue.JobManager;

import java.io.File;

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
    private String pdfPath;

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
        pdfPath = getIntent().getStringExtra("filePath");
    }

    @OnClick(R.id.fab_sign)
    public void onClick() {
//        alertDialog.show();

        
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pdfPath != null) {
            pdfView.fromFile(new File(pdfPath))
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();

        } else {
            Toast.makeText(this, "Choose file first", Toast.LENGTH_LONG);
            Intent i = new Intent(ViewDocumentActivity.this, SignedDocumentActivity.class);
            startActivity(i);

        }


    }


    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
}
