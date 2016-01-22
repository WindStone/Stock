/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

/**
 * @author yuanren.syr
 * @version $Id: FileBundleView.java, v 0.1 2016/1/12 0:20 yuanren.syr Exp $
 */
public class FileBundleView {

    /** 文件名 */
    private String fileName;

    /** 文件链接 */
    private String fileUrl;

    public FileBundleView(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
