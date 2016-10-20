/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.MinuteTradeDAO;
import stock.common.dal.dataobject.MinuteTradeData;

/**
 * @author yuanren.syr
 * @version $Id: IbatisMinuteTradeDAO.java, v 0.1 2016/3/7 16:16 yuanren.syr Exp $
 */
public class IbatisMinuteTradeDAO extends SqlMapClientDaoSupport implements MinuteTradeDAO {

    public void insert(MinuteTradeData minuteTradeData) {
        getSqlMapClientTemplate().insert("MS-STOCK-MINUTE-INSERT", minuteTradeData);
    }
}
