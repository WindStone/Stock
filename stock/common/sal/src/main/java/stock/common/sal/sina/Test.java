package stock.common.sal.sina;

import stock.common.sal.model.CurrentTradeData;

/**
 * Created by songyuanren on 2016/7/13.
 */
public class Test {
    public static void main(String[] args) {
        SinaStockClient client = new SinaStockClientImpl();
        CurrentTradeData ctd = client.getStock("sz002769");
        System.out.println(ctd);
    }
}
