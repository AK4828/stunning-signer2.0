package org.meruvian.ldsigner.entity;

import java.io.File;

/**
 * Created by dianw on 8/27/15.
 */
public class KeyStore {
    private String location;
    private String type;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExist() {
        return new File(location).exists();
    }
}
