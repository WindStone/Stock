/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import stock.common.dal.enums.SequenceEnum;

/**
 * @author yuanren.syr
 * @version $Id: SequenceGenerator.java, v 0.1 2015/12/6 16:47 yuanren.syr Exp $
 */
public interface SequenceGenerator {

    String getSequence(SequenceEnum sequenceEnum);
}
