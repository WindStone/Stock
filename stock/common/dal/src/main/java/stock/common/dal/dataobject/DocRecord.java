/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

/**
 * @author yuanren.syr
 * @version $Id: DocRecord.java, v 0.1 2016/2/25 15:05 yuanren.syr Exp $
 */
public class DocRecord {

    private String docRecordId;

    private String docTitle;

    private String docTemplate;

    private Date   gmtCreate;

    private Date   gmtModified;

    public String getDocRecordId() {
        return docRecordId;
    }

    public void setDocRecordId(String docRecordId) {
        this.docRecordId = docRecordId;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocTemplate() {
        return docTemplate;
    }

    public void setDocTemplate(String docTemplate) {
        this.docTemplate = docTemplate;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
