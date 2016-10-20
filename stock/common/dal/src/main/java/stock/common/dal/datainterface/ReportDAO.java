package stock.common.dal.datainterface;

import stock.common.dal.dataobject.Report;

import java.util.List;

/**
 * Created by songyuanren on 2016/10/17.
 */
public interface ReportDAO {

    void insert(Report report);

    void updateById(Report report);

    Report findByUsernameAndDate(String username, String date);

    List<Report> findAll(Integer limitStart, Integer limitEnd);

}
