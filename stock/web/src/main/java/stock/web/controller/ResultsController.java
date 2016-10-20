/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import stock.common.util.PathUtil;
import stock.web.view.FileBundleView;
import stock.web.view.ResultBundleView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: ResultsController.java, v 0.1 2016/1/11 1:55 yuanren.syr Exp $
 */
@Controller
public class ResultsController {

    private String[] prefix = new String[]{"最高点峰值10%内涨停排序预测", "距2.18最高点跌幅超过25%按跌幅排序预测",
            "最近两个月超跌25%预测", "最近两个月超涨33%预测"};

    private String[] suffix = new String[]{"预测", "预测", "预测", "预测"};

    @RequestMapping(value = {"/results.json"}, method = {RequestMethod.GET})
    public String getResult(Map<String, Object> model, HttpServletRequest httpServletRequest) {
        List<ResultBundleView> resultBundleViews = getResultBundlesView(false);
        List<JSONObject> result = Lists.transform(resultBundleViews, new Function<ResultBundleView, JSONObject>() {
            public JSONObject apply(ResultBundleView resultBundleView) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", resultBundleView.getDate());
                List<Boolean> fileExists = Lists.transform(Lists.newArrayList(resultBundleView.getFileBundleViews()),
                        new Function<FileBundleView, Boolean>() {
                            public Boolean apply(FileBundleView fileBundleView) {
                                return fileBundleView != null;
                            }
                        });
                List<String> fileNames = Lists.transform(Lists.newArrayList(prefix),
                        new Function<String, String>() {
                            public String apply(String titleName) {
                                try {
                                    return URLEncoder.encode(titleName, "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    return null;
                                }
                            }
                        });
                jsonObject.put("fileList", fileExists);
                jsonObject.put("fileNames", fileNames);
                return jsonObject;
            }
        });
        model.put("callback", httpServletRequest.getParameter("callback"));
        model.put("json", JSON.toJSONString(result));
        return "/statistics/index";
    }

    @RequestMapping(value = {"/results.html"}, method = {RequestMethod.GET})
    public String showResult(Map<String, Object> model) {

        List<ResultBundleView> resultBundlesView = getResultBundlesView(true);
        String[] titles = new String[prefix.length];
        String[] encodeTitles = new String[prefix.length];
        for (int i = 0; i < prefix.length; ++i) {
            titles[i] = prefix[i] + StringUtils.substringBefore(suffix[i], ".");
            try {
                encodeTitles[i] = URLEncoder.encode(titles[i], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        model.put("titles", titles);
        model.put("encodeTitles", encodeTitles);
        model.put("resultBundles", resultBundlesView);

        return "results";
    }

    private List<ResultBundleView> getResultBundlesView(final boolean inOrder) {
        File folder = new File(PathUtil.getExcelPath());

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
                    if (getIndex(fileName) == -1) {
                        continue;
                    }
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
                return inOrder ? o1.getDate().compareTo(o2.getDate()) : o2.getDate().compareTo(o1.getDate());
            }
        });

        return resultBundlesView;
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
