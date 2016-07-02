package org.meruvian.ldsigner.job;

import android.security.KeyChain;
import android.security.KeyChainException;
import android.util.Log;

import org.meruvian.ldsigner.LdsignerApplication;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.security.cert.X509Certificate;

import de.greenrobot.event.EventBus;

/**
 * Created by akm on 08/06/16.
 */
public class CertificateChainJob extends Job {

    private String subjectDN;
    private String issuer;
    private String expire;
    private String alias;

    public CertificateChainJob(String alias) {
        super(new Params(1).persist());
        this.alias = alias;
        Log.d("AAAA", "ASDQWE1");
    }

    @Override
    public void onAdded() {

        Log.d("AAAA", "ASDQWE2");

        EventBus.getDefault().post(new CertificateChainEvent(subjectDN, issuer, expire, JobStatus.ADDED));

    }

    @Override
    public void onRun() throws Throwable {
        try {

            X509Certificate[] chain = new X509Certificate[0];
            chain = KeyChain.getCertificateChain(LdsignerApplication.getInstance(), alias);

            X509Certificate cert = chain[0];

            subjectDN = cert.getSubjectDN().getName();
            issuer = cert.getIssuerDN().getName();
            expire = cert.getNotAfter().toString();

            EventBus.getDefault().post(new CertificateChainEvent(subjectDN, issuer, expire, JobStatus.SUCCESS));

        } catch (KeyChainException e) {
            EventBus.getDefault().post(new CertificateChainEvent(subjectDN, issuer, expire, JobStatus.SYSTEM_ERROR));
            e.printStackTrace();
        } catch (InterruptedException e) {
            EventBus.getDefault().post(new CertificateChainEvent(subjectDN, issuer, expire, JobStatus.SYSTEM_ERROR));
            e.printStackTrace();
        }

    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new CertificateChainEvent(subjectDN, issuer, expire, JobStatus.SYSTEM_ERROR));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class CertificateChainEvent {
        private String subjectDN;
        private String issuer;
        private String expire;

        private int status;

        public CertificateChainEvent(String subjectDN,String issuer, String expire, int status) {
            this.subjectDN = subjectDN;
            this.issuer = issuer;
            this.expire = expire;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String getSubjectDN() {
            return subjectDN;
        }

        public String getIssuer() {
            return issuer;
        }

        public String getExpire() {
            return expire;
        }
    }
}
