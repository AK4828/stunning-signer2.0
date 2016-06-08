package org.meruvian.ldsigner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.meruvian.ldsigner.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements KeyChainAliasCallback {

    private SharedPreferences preferences;

    @Bind(R.id.btn_browse) Button browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @OnClick(R.id.btn_browse)
    public void onCLick() {
        KeyChain.choosePrivateKeyAlias(this, this, new String[] {}, null, null, -1, null);
    }

    @Override
    public void alias(String alias) {
        preferences.edit().putString("alias", alias).apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.contains("alias")) {
            Intent intent = new Intent(MainActivity.this, ShowDetailCertificateActivity.class);
            startActivity(intent);
        }
    }

}
