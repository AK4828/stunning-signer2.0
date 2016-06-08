package com.meruvian.ldsigner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.security.KeyChainException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.meruvian.ldsigner.R;
import com.meruvian.ldsigner.entity.KeyStore;
import com.meruvian.ldsigner.utils.AuthenticationUtils;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

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

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.contains("alias")) {
            try {
                String alias = preferences.getString("alias", "");
                Log.d("ALIAS", alias);
                showCertificate(alias);
            } catch (KeyChainException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_browse)
    public void onCLick() {
        KeyChain.choosePrivateKeyAlias(this, this, new String[] {}, null, null, -1, null);
    }

    @Override
    public void alias(String alias) {
        preferences.edit().putString("alias", alias).apply();
    }


    private void showCertificate(String alias) throws KeyChainException, InterruptedException {
//        X509Certificate[] chain = KeyChain.getCertificateChain(this, alias);
//        X509Certificate cert = chain[0];
//
//        Log.d("DN", cert.getSubjectDN().toString());
    }
}
