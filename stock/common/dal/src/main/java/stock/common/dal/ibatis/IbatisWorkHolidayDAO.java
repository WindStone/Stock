/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.WorkHolidayDAO;
import stock.common.dal.dataobject.WorkHolidayConfig;

/**
 * @author yuanren.syr
 * @version $Id: IbatisWorkHolidayDAO.java, v 0.1 2016/2/3 17:43 yuanren.syr Exp $
 */
public class IbatisWorkHolidayDAO extends SqlMapClientDaoSupport implements WorkHolidayDAO {

    public List<WorkHolidayConfig> queryWorkHolidays() {
        return getSqlMapClientTemplate().queryForList("WORK-HOLIDAY-CONFIG-QUERY");
    }
}
