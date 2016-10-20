package stock.common.dal.dataobject;

/**
 * Created by songyuanren on 2016/8/16.
 */
public class DynamicOverboughtWrapper extends DailyTradeData {

    private double overboughtRate;

    private double lowestClosingPrice;

    private String lowestClosingDate;

    public DynamicOverboughtWrapper(DailyTradeData dtd) {
        super(dtd);
    }

    public double getOverboughtRate() {
        return overboughtRate;
    }

    public void setOverboughtRate(double overboughtRate) {
        this.overboughtRate = overboughtRate;
    }

    public double getLowestClosingPrice() {
        return lowestClosingPrice;
    }

    public void setLowestClosingPrice(double lowestClosingPrice) {
        this.lowestClosingPrice = lowestClosingPrice;
    }

    public String getLowestClosingDate() {
        return lowestClosingDate;
    }

    public void setLowestClosingDate(String lowestClosingDate) {
        this.lowestClosingDate = lowestClosingDate;
    }
}
