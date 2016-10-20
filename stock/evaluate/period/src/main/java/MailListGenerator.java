/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */

import java.net.URL;
import java.text.MessageFormat;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;

import com.google.common.collect.Sets;

/**
 * @author yuanren.syr
 * @version $Id: MailListGenerator.java, v 0.1 2016/4/25 12:27 yuanren.syr Exp $
 */
public class MailListGenerator {

    private static String sysNameStr = "merchant|prodtrans|mobilecashier|clientcashier|mobileacauth|authcenter|mobilepayfront|mobilechannel|securitycore|securitydata|securityidentify|ctu|wapcashier|maliprod|cashier|pointcore|pcreditcore|tradecore|escrowexprod|mobilebc|financingcore|cifcommon|personalprod|mobileprod|cnctu|financeprod|payacceptance|rulecenter|paydecision|promoadprod|excashier|mrexcashier|mobilediscovery|minitrans|yebcore|yebbatch|discount|liveservice|yebprod|zdatabus|transfercenter|baitiaocore|pcreditprod|mobilechat|voucherprod|vouchercore|promoprod|consumecenter|mobilerelation|jmeter|pcreditdecision|pcreditpay|couponcore|modelcenter|mobileic|mctu|mobileapp|giftprod|mobilepromo|prizecore|promocore|mcomment|promorulecenter|adatabus|transferprod|settlequery|zmodelcenter";

    public static void main(String[] args) {
        String[] sysNames = StringUtils.split(sysNameStr, "|");
        Set<String> ownerNameList = Sets.newHashSet();
        Set<String> archNameList = Sets.newHashSet();
        for (String sysName : sysNames) {
            String warrantUrl = MessageFormat.format(
                "https://zappinfo.alipay.com/app/showApp.htm?type=basic&appName={0}", sysName);
            try {
                URL url = new URL(warrantUrl);
                Parser parser = new Parser(url.openConnection());
                parser.setEncoding("gb2312");
                NodeFilter tableFilter = new NodeFilter() {
                    public boolean accept(Node node) {
                        if (node.getText().startsWith("table class=\"table table-bordered\"")) {
                            return true;
                        }
                        return false;
                    }
                };
                NodeList tables = parser.extractAllNodesThatMatch(tableFilter);
                if (tables.size() == 0) {
                    continue;
                }
                NodeList trs = tables.elementAt(0).getChildren()
                    .extractAllNodesThatMatch(new NodeClassFilter(TableRow.class));
                Node ownerTr = trs.elementAt(1);
                Node archTr = trs.elementAt(2);
                NodeList ownerTds = ownerTr.getChildren().extractAllNodesThatMatch(
                    new NodeClassFilter(TableColumn.class));
                NodeList archTds = archTr.getChildren().extractAllNodesThatMatch(
                    new NodeClassFilter(TableColumn.class));
                Node ownerTd = ownerTds.elementAt(1);
                Node archTd = archTds.elementAt(1);
                Node ownerName = ownerTd.getChildren()
                    .extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class)).elementAt(0);
                Node archName = archTd.getChildren()
                    .extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class)).elementAt(0);
                String ownerNameStr = ((LinkTag) ownerName).getStringText().trim();
                String archNameStr = ((LinkTag) archName).getStringText().trim();
                ownerNameList.add(ownerNameStr);
                archNameList.add(archNameStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String ownerName : ownerNameList) {
            System.out.print(ownerName + ";");
        }
        System.out.println();
        for (String archName : archNameList) {
            System.out.print(archName + ";");
        }
    }
}
