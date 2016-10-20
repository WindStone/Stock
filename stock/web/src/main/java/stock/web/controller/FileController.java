/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yuanren.syr
 * @version $Id: FileController.java, v 0.1 2016/1/11 2:29 yuanren.syr Exp $
 */
@Controller
public class FileController {

    //Spring这里是通过实现ServletContextAware接口来注入ServletContext对象
    private ServletContext servletContext;

    @RequestMapping(value = "/file.html", method = RequestMethod.GET)
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = request.getParameter("filename");
            filename = new String(filename.getBytes("iso-8859-1"), "UTF-8");
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("application/vnd.ms-excel");
            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(filename.getBytes("utf-8"), "iso-8859-1"));

            ServletOutputStream out;
            //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
            String dateStr = StringUtils.substringBetween(filename, "_", ".");
            File file = new File(System.getProperty("user.home") + File.separatorChar + "resources"
                    + File.separatorChar + dateStr + File.separatorChar + filename);

            FileInputStream inputStream = new FileInputStream(file);

            //3.通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();

            int b = 0;
            byte[] buffer = new byte[512];
            while (b != -1) {
                b = inputStream.read(buffer);
                //4.写到输出流(out)中
                out.write(buffer, 0, b);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
