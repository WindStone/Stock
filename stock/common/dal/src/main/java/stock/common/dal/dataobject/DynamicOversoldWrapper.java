package stock.common.dal.dataobject;

/**
 * Created by songyuanren on 2016/7/24.
 */
public class DynamicOversoldWrapper extends DailyTradeData {

    private double oversoldRate;

    private double highestClosingPrice;

    private String highestClosingDate;

    public DynamicOversoldWrapper(DailyTradeData dtd) {
        super(dtd);
    }

    public double getOversoldRate() {
        return oversoldRate;
    }

    public void setOversoldRate(double oversoldRate) {
        this.oversoldRate = oversoldRate;
    }

    public double getHighestClosingPrice() {
        return highestClosingPrice;
    }

    public void setHighestClosingPrice(double highestClosingPrice) {
        this.highestClosingPrice = highestClosingPrice;
    }

    public String getHighestClosingDate() {
        return highestClosingDate;
    }

    public void setHighestClosingDate(String highestClosingDate) {
        this.highestClosingDate = highestClosingDate;
    }
}
