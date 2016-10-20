/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

/**
 * @author yuanren.syr
 * @version $Id: RecordRelation.java, v 0.1 2016/2/25 15:06 yuanren.syr Exp $
 */
public class RecordRelation {

    private String recordRelationId;

    private String recordType;

    private String recordFromId;

    private String recordToId;

    public String getRecordRelationId() {
        return recordRelationId;
    }

    public void setRecordRelationId(String recordRelationId) {
        this.recordRelationId = recordRelationId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordFromId() {
        return recordFromId;
    }

    public void setRecordFromId(String recordFromId) {
        this.recordFromId = recordFromId;
    }

    public String getRecordToId() {
        return recordToId;
    }

    public void setRecordToId(String recordToId) {
        this.recordToId = recordToId;
    }
}
