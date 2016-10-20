package stock.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import stock.common.dal.datainterface.ReportDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.datainterface.UserDAO;
import stock.common.dal.dataobject.Report;
import stock.common.dal.dataobject.User;
import stock.common.dal.enums.SequenceEnum;
import stock.common.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by songyuanren on 2016/10/16.
 */
@Controller
public class ReportsController {

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private UserDAO userDAO;

    private static final Date startDate = DateUtil.parseSimpleDate("2016-10-10");

    /**
     * 返回格式
     * {
     * users: [{username: aaa, realname: bbb}, ...],
     * reports: [{date: '2016-10-01 ~ 2016-10-05', 'username': true/false}]
     * }
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = {"/getAllReports.html"}, method = {RequestMethod.GET})
    public String getAllReports(Map<String, Object> model, HttpServletRequest request) throws UnsupportedEncodingException {
        List<Report> reportList = reportDAO.findAll(null, null);
        List<String> dateList = buildDate(startDate, new Date());
        List<User> userList = userDAO.findUsers();

        Map<String, Object> userReportMap = Maps.newHashMap();
        JSONArray userArray = new JSONArray();

        for (User user : userList) {
            userReportMap.put(user.getUsername(), false);
            Map<String, String> userNameMap = Maps.newHashMap();
            userNameMap.put("username", URLEncoder.encode(user.getUsername(), "UTF-8"));
            userNameMap.put("realname", user.getRealname());
            userArray.add(userNameMap);
        }

        List<Map<String, Object>> reportResultList = Lists.newArrayList();
        for (String date : dateList) {
            Map<String, Object> map = Maps.newHashMap(userReportMap);
            map.put("date", date);
            reportResultList.add(map);
        }

        // 反向填充
        int index = reportResultList.size() - 1;
        for (Report report : reportList) {
            for (; index >= 0; --index) {
                if (StringUtils.equals((String) reportResultList.get(index).get("date"), report.getDate())) {
                    if (!StringUtils.isEmpty(report.getContent())) {
                        reportResultList.get(index).put(report.getUsername(), true);
                    }
                    break;
                }
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", userArray);
        jsonObject.put("reports", reportResultList);

        model.put("callback", request.getParameter("callback"));
        model.put("json", jsonObject.toJSONString());
        return "stockcode";
    }

    @RequestMapping(value = {"/getReport.html"}, method = {RequestMethod.GET})
    public String getReport(Map<String, Object> model, @RequestParam("username") String username,
                            @RequestParam("date") String date, HttpServletRequest request) throws UnsupportedEncodingException {
        date = URLDecoder.decode(date, "UTF-8");
        Report report = reportDAO.findByUsernameAndDate(username, date);
        model.put("callback", request.getParameter("callback"));
        model.put("json", report == null ? "\"\"" : "\"" + URLEncoder.encode(report.getContent(), "UTF-8") + "\"");
        return "stockcode";
    }

    @RequestMapping(value = {"/saveReport.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public void submitReport(@RequestParam("username") String username, @RequestParam("date") String date,
                             @RequestParam("content") String content) {
        Report report = reportDAO.findByUsernameAndDate(username, date);
        if (report == null) {
            report = new Report();
            report.setId(sequenceGenerator.getSequence(SequenceEnum.REPORT_SEQ));
            report.setUsername(username);
            report.setDate(date);
            report.setContent(content);
            reportDAO.insert(report);
        } else {
            report.setContent(content);
            reportDAO.updateById(report);
        }
    }

    private List<String> buildDate(Date startDate, Date endDate) {
        List<String> result = Lists.newLinkedList();
        while (startDate.getTime() < endDate.getTime()) {
            result.add(0, DateUtil.simpleFormat(startDate) + " ~ " + DateUtil.simpleFormat(DateUtils.addDays(startDate, 4)));
            startDate = DateUtils.addDays(startDate, 7);
        }
        return result;
    }
}
