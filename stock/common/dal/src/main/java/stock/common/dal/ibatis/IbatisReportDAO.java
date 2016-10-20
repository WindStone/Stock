package stock.common.dal.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import stock.common.dal.datainterface.ReportDAO;
import stock.common.dal.dataobject.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songyuanren on 2016/10/17.
 */
public class IbatisReportDAO extends SqlMapClientDaoSupport implements ReportDAO {

    public void insert(Report report) {
        getSqlMapClientTemplate().insert("MS-REPORT-INSERT", report);
    }

    public void updateById(Report report) {
        getSqlMapClientTemplate().update("MS-REPORT-UPDATE-BY-ID", report);
    }

    public Report findByUsernameAndDate(String username, String date) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("username", username);
        map.put("date", date);

        return (Report) getSqlMapClientTemplate().queryForObject("MS-REPORT-QUERY-BY-USERNAME-AND-DATE", map);
    }

    public List<Report> findAll(Integer limitStart, Integer limitEnd) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("limitStart", limitStart);
        map.put("limitEnd", limitEnd);

        return (List) getSqlMapClientTemplate().queryForList("MS-REPORT-QUERY-ALL", map);
    }

}
