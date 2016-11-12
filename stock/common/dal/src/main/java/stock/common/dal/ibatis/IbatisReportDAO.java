package stock.common.dal.ibatis;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.ReportDAO;
import stock.common.dal.dataobject.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songyuanren on 2016/10/17.
 */
public class IbatisReportDAO extends SqlSessionDaoSupport implements ReportDAO {

    public void insert(Report report) {
        getSqlSession().insert("MS-REPORT-INSERT", report);
    }

    public void updateById(Report report) {
        getSqlSession().update("MS-REPORT-UPDATE-BY-ID", report);
    }

    public Report findByUsernameAndDate(String username, String date) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("username", username);
        map.put("date", date);

        return (Report) getSqlSession().selectOne("MS-REPORT-QUERY-BY-USERNAME-AND-DATE", map);
    }

    public List<Report> findAll(Integer limitStart, Integer limitEnd) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("limitStart", limitStart);
        map.put("limitEnd", limitEnd);

        return (List) getSqlSession().selectList("MS-REPORT-QUERY-ALL", map);
    }

}
