package stock.core.model.models;

import stock.core.model.enums.FilterGroupEnum;

import java.util.List;

/**
 * Created by songyuanren on 2016/8/8.
 */
public class StockCodeFilterParam extends FilterParam {

    private List<FilterGroupEnum> filterGroups;

    private List<String> filterCodes;

    public List<FilterGroupEnum> getFilterGroups() {
        return filterGroups;
    }

    public void setFilterGroups(List<FilterGroupEnum> filterGroups) {
        this.filterGroups = filterGroups;
    }

    public List<String> getFilterCodes() {
        return filterCodes;
    }

    public void setFilterCodes(List<String> filterCodes) {
        this.filterCodes = filterCodes;
    }

    public int getMaxWindow() {
        return 0;
    }
}
