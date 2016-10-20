package stock.core.model.models;

import stock.core.model.enums.SymbolEnum;

/**
 * Created by songyuanren on 2016/8/8.
 */
public class VolumeFilterParam extends FilterParam {

    private SymbolEnum volumeSymbol;

    private int volumeWindow;

    private double volumePercent;

    public SymbolEnum getVolumeSymbol() {
        return volumeSymbol;
    }

    public void setVolumeSymbol(SymbolEnum volumeSymbol) {
        this.volumeSymbol = volumeSymbol;
    }

    public int getVolumeWindow() {
        return volumeWindow;
    }

    public void setVolumeWindow(int volumeWindow) {
        this.volumeWindow = volumeWindow;
    }

    public double getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(double volumePercent) {
        this.volumePercent = volumePercent;
    }

    public int getMaxWindow() {
        return volumeWindow;
    }
}
