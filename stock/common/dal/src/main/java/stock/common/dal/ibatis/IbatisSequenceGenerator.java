/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.enums.SequenceEnum;

/**
 * @author yuanren.syr
 * @version $Id: IbatisSequenceGenerator.java, v 0.1 2015/12/6 16:50 yuanren.syr Exp $
 */
public class IbatisSequenceGenerator extends SqlSessionDaoSupport implements SequenceGenerator {

    public String getSequence(SequenceEnum sequenceEnum) {
        String val = getSqlSession().selectOne("MS-SEQUENCE-NEXT-VAL",
                sequenceEnum.getName());
        return StringUtils.leftPad(val, sequenceEnum.getLength(), '0');
    }
}
