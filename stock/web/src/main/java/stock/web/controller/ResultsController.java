/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import stock.web.view.FileBundleView;
import stock.web.view.ResultBundleView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: ResultsController.java, v 0.1 2016/1/11 1:55 yuanren.syr Exp $
 */
@Controller
public class ResultsController {

    private String[] prefix = new String[] { "距10.01最高点峰值10%内涨停排序", "距10.01最高点峰值10%内涨停排序",
            "10.01最高点跌幅超过25%按跌幅排序" };

    private String[] suffix = new String[] { "预测", "评估", "预测" };

    @RequestMapping(value = { "/results.html" }, method = { RequestMethod.GET })
    public String showResult(Map<String, Object> model) {

        File folder = new File(System.getProperty("user.home") + File.separatorChar + "resources"
                               + File.separatorChar);

        List<ResultBundleView> resultBundlesView = Lists.newArrayList();
        for (File dateFolder : folder.listFiles()) {
            ResultBundleView resultBundleView = new ResultBundleView();
            String dateStr = dateFolder.getName();
            resultBundleView.setDate(dateStr);
            if (!dateFolder.isDirectory() || dateFolder.listFiles() == null) {
                continue;
            }
            List<String> fileNames = Lists.transform(Lists.newArrayList(dateFolder.listFiles()),
                new Function<File, String>() {
                    public String apply(File file) {
                        return file.getName();
                    }
                });
            resultBundleView.setFileBundleViews(new FileBundleView[prefix.length]);
            for (String fileName : fileNames) {
                try {
                    resultBundleView.getFileBundleViews()[getIndex(fileName)] = new FileBundleView(
                        fileName, URLEncoder.encode(fileName, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            resultBundlesView.add(resultBundleView);
        }
        Collections.sort(resultBundlesView, new Comparator<ResultBundleView>() {
            public int compare(ResultBundleView o1, ResultBundleView o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        String[] titles = new String[prefix.length];
        for (int i = 0; i < prefix.length; ++i) {
            titles[i] = prefix[i] + "-" + StringUtils.substringBefore(suffix[i], ".");
        }
        model.put("titles", titles);
        model.put("resultBundles", resultBundlesView);

        return "results";
    }

    private int getIndex(String fileName) {
        for (int i = 0; i < prefix.length; ++i) {
            if (StringUtils.contains(fileName, prefix[i])
                && StringUtils.contains(fileName, suffix[i])) {
                return i;
            }
        }
        return -1;
    }
}
