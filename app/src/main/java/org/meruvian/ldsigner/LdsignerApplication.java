package org.meruvian.ldsigner;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import org.meruvian.ldsigner.entity.DaoMaster;
import org.meruvian.ldsigner.entity.DaoSession;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Created by akm on 06/06/16.
 */
public class LdsignerApplication extends Application {

    public static LdsignerApplication instance;
    private JobManager jobManager;
    private ObjectMapper objectMapper;
    private DaoSession daoSession;

    public LdsignerApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        Iconify.with(new FontAwesomeModule());
        configureJobManager();
        configureDatabase();
    }

    private void configureDatabase() {
        DaoMaster.OpenHelper helper = new DaoMaster.OpenHelper(this, AppVariables.databaseName, null) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public static LdsignerApplication getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
