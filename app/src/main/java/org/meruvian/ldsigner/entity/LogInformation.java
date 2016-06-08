package org.meruvian.ldsigner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by root on 28/10/14.
 */
public class LogInformation implements Serializable {
    private Date createDate;
    private String createBy;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

}
