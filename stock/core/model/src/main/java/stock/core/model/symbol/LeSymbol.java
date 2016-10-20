package stock.core.model.symbol;

/**
 * Created by songyuanren on 2016/8/9.
 */
public class LeSymbol implements CompareSymbol {

    public boolean compare(double a, double b, double percent) {
        return a <= percent * b / 100;
    }
}
