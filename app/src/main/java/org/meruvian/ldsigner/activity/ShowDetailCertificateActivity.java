package org.meruvian.ldsigner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconButton;
import org.meruvian.ldsigner.LdsignerApplication;
import org.meruvian.ldsigner.job.CertificateChainJob;
import org.meruvian.ldsigner.job.JobStatus;

import com.meruvian.ldsigner.R;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by akm on 08/06/16.
 */
public class ShowDetailCertificateActivity extends AppCompatActivity implements KeyChainAliasCallback {

    @Bind(R.id.btn_resume) IconButton resumeButton;
    @Bind(R.id.btn_change) IconButton changeButton;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.txt_subject_dn) TextView txtSubjectDN;
    @Bind(R.id.txt_issuer) TextView txtIssuer;
    @Bind(R.id.txt_expire) TextView txtExpire;
    private SharedPreferences preferences;
    private JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_certificate_activity);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setSupportActionBar(toolbar);

        String alias = preferences.getString("alias", "");
        jobManager = LdsignerApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new CertificateChainJob(alias));

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

    public void onEventMainThread(CertificateChainJob.CertificateChainEvent event) {
        if (event.getStatus() == JobStatus.SUCCESS) {

            txtSubjectDN.setText(event.getSubjectDN());
            txtIssuer.setText(event.getIssuer());
            txtExpire.setText(event.getExpire());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_resume)
    public void onClick() {
        Intent intent = new Intent(ShowDetailCertificateActivity.this, SignedDocumentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_change)
    public void onClickChange() {
        KeyChain.choosePrivateKeyAlias(this, this, new String[]{}, null, null, -1, null);
    }

    @Override
    public void alias(String alias) {
        preferences.edit().putString("alias", alias).apply();
    }

}
