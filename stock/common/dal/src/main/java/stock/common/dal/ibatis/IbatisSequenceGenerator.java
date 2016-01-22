/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.enums.SequenceEnum;

import java.util.HashMap;

/**
 * @author yuanren.syr
 * @version $Id: IbatisSequenceGenerator.java, v 0.1 2015/12/6 16:50 yuanren.syr Exp $
 */
public class IbatisSequenceGenerator extends SqlMapClientDaoSupport implements SequenceGenerator {

    public String getSequence(SequenceEnum sequenceEnum) {
        String val = (String) getSqlMapClientTemplate().queryForObject("MS-SEQUENCE-NEXT-VAL",
            sequenceEnum.getName());
        return StringUtils.leftPad(val, sequenceEnum.getLength(), '0');
    }
}
