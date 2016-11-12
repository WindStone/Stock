/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.WorkHolidayDAO;
import stock.common.dal.dataobject.WorkHolidayConfig;

import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: IbatisWorkHolidayDAO.java, v 0.1 2016/2/3 17:43 yuanren.syr Exp $
 */
public class IbatisWorkHolidayDAO extends SqlSessionDaoSupport implements WorkHolidayDAO {

    public List<WorkHolidayConfig> queryWorkHolidays() {
        return getSqlSession().selectList("WORK-HOLIDAY-CONFIG-QUERY");
    }
}
