package org.meruvian.ldsigner.job;

import android.os.Environment;

import org.meruvian.ldsigner.LdsignerApplication;
import org.meruvian.ldsigner.entity.DaoSession;
import org.meruvian.ldsigner.entity.Document;
import org.meruvian.ldsigner.entity.DocumentDao;
import org.meruvian.ldsigner.entity.KeyStore;
import org.meruvian.ldsigner.entity.SignedDocument;
import org.meruvian.ldsigner.entity.SignedDocumentDao;
import org.meruvian.ldsigner.utils.AuthenticationUtils;
import com.meruvian.toolkit_core.cms.CmsSigner;
import com.meruvian.toolkit_core.commons.KeyPairUtils;
import com.meruvian.toolkit_core.commons.KeyStoreUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.UUID;

import de.greenrobot.event.EventBus;
/**
 * Created by akm on 15/10/15.
 */
public class DocumentSignJob extends Job {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Long docId;
    private String password;
    private String filePath;

    public static DocumentSignJob newInstance(Long docId, String password, String filePath) {
        DocumentSignJob job = new DocumentSignJob();

        job.docId = docId;
        job.password = password;
        job.filePath = filePath;

        return job;
    }

    public DocumentSignJob(){
        super(new Params(1).requireNetwork().persist());

    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new DocumentSignEvent(password, filePath, JobStatus.ADDED));
    }

    @Override
    public void onRun() throws Throwable {
        KeyStore keyStore = AuthenticationUtils.getKeyStore();
        try {
            FileInputStream p12File = new FileInputStream(new File(keyStore.getLocation()));
            char[] certPassword = password.toCharArray();
            java.security.KeyStore ks = KeyStoreUtils.getKeyStore(p12File, certPassword, keyStore.getType());

            KeyPair keyPair = KeyPairUtils.getKeyPair(ks, certPassword);
            X509Certificate cert = KeyStoreUtils.getCertificate(ks);
            Assert.assertNotNull(keyPair);
            Assert.assertNotNull(cert);

            File inputFile = new File(filePath);
            File outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = new File(outputPath, UUID.randomUUID().toString() + ".p7b");

            CmsSigner signer = new CmsSigner(ks,certPassword);
            signer.sign(inputFile, outputFile);

            DaoSession daoSession = LdsignerApplication.getInstance().getDaoSession();
            DocumentDao documentDao = daoSession.getDocumentDao();
            SignedDocumentDao dao = daoSession.getSignedDocumentDao();

            Document document = documentDao.load(docId);

            SignedDocument signedDocument = new SignedDocument();
            signedDocument.setDocument(document);
            signedDocument.setSignatureType("PKCS7");
            signedDocument.setSignatureBlob(FileUtils.readFileToByteArray(outputFile));

            dao.insert(signedDocument);

            EventBus.getDefault().post(new DocumentSignEvent(password, filePath, JobStatus.SUCCESS));

        } catch (Exception e) {
            EventBus.getDefault().post(new DocumentSignEvent(password, filePath, JobStatus.USER_ERROR));
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        EventBus.getDefault().post(new DocumentSignEvent(password, filePath , JobStatus.SYSTEM_ERROR));
        return false;
    }

    public static class DocumentSignEvent {
        private String password;
        private String filePath;

        private int status;

        public DocumentSignEvent(String password, String filePath, int status) {
            this.filePath = filePath;
            this.password = password;
            this.status = status;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getPassword() {
            return password;
        }

        public int getStatus() {
            return status;
        }
    }
}