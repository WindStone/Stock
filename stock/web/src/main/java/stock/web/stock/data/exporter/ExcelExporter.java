/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import stock.common.util.DateUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: ExcelExporter.java, v 0.1 2016/1/11 1:11 yuanren.syr Exp $
 */
public class ExcelExporter implements Exporter {
    public void writeResult(List<List<String>> results, List<TitleLabel> titleRow, String fileName,
                            Date currentDate) {
        FileOutputStream fos = null;
        WritableWorkbook wwb = null;
        try {
            File folder = new File(System.getProperty("user.home") + File.separatorChar
                                   + "resources" + File.separatorChar
                                   + DateUtil.simpleFormat(currentDate));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath() + File.separatorChar + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(fos);
            WritableSheet sheet = wwb.createSheet(fileName, 0);
            int curCol = 0;
            for (TitleLabel titleLabel : titleRow) {
                if (titleLabel.getColumn() != 1) {
                    sheet.mergeCells(curCol, 0, curCol + titleLabel.getColumn() - 1, 0);
                }
                Label label = new Label(curCol, 0, titleLabel.getContent());
                sheet.addCell(label);
                curCol += titleLabel.getColumn();
            }

            for (int i = 0; i < results.size(); ++i) {
                for (int j = 0; j < results.get(i).size(); ++j) {
                    Label label = new Label(j, i + 1, results.get(i).get(j));
                    sheet.addCell(label);
                }
            }

            wwb.write();

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (wwb != null) {
                    wwb.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Throwable t) {
            }
        }
    }

    public void writeContentResult(List<TitleLabel> titleRow, List<List<ContentLabel>> results,
                                   String fileName, Date currentDate) {
        FileOutputStream fos = null;
        WritableWorkbook wwb = null;
        try {
            File folder = new File(System.getProperty("user.home") + File.separatorChar
                                   + "resources" + File.separatorChar
                                   + DateUtil.simpleFormat(currentDate));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath() + File.separatorChar + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(fos);
            WritableSheet sheet = wwb.createSheet(fileName, 0);
            int curCol = 0;
            for (TitleLabel titleLabel : titleRow) {
                if (titleLabel.getColumn() != 1) {
                    sheet.mergeCells(curCol, 0, curCol + titleLabel.getColumn() - 1, 0);
                }
                Label label = new Label(curCol, 0, titleLabel.getContent());
                sheet.addCell(label);
                curCol += titleLabel.getColumn();
            }

            for (int i = 0; i < results.size(); ++i) {
                int contentCurCol = 0;
                for (int j = 0; j < results.get(i).size(); ++j) {
                    ContentLabel contentLabel = results.get(i).get(j);
                    if (contentLabel.getColumn() != 1) {
                        sheet.mergeCells(contentCurCol, i + 1,
                            contentCurCol + contentLabel.getColumn() - 1, i + 1);
                    }
                    Label label = new Label(contentCurCol, i + 1, contentLabel.getContent());
                    sheet.addCell(label);
                    contentCurCol += contentLabel.getColumn();
                }
            }

            wwb.write();

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (wwb != null) {
                    wwb.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Throwable t) {
            }
        }
    }

}
