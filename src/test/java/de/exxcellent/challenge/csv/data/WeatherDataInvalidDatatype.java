package de.exxcellent.challenge.csv.data;

import de.exxcellent.challenge.csv.CsvProperty;

/**
 * Weather data class.
 *
 * @author Kevin Degen
 */
public class WeatherDataInvalidDatatype {

    @CsvProperty("Day")
    private int day;
    @CsvProperty("MxT")
    private int maxTemp;
    @CsvProperty("MnT")
    private int minTemp;
    @CsvProperty("AvT")
    private int averageTemp;
    @CsvProperty("AvDP")
    private float avdp;
    @CsvProperty("1HrP TPcpn")
    private int tpcpn;
    @CsvProperty("PDir")
    private int pdir;
    @CsvProperty("AvSp")
    private double avsp;
    @CsvProperty("Dir")
    private int dir;
    @CsvProperty("MxS")
    private int mxs;
    @CsvProperty("SkyC")
    private double skyc;
    @CsvProperty("MxR")
    private int mxr;
    @CsvProperty("Mn")
    private int mn;
    @CsvProperty("R AvSLP")
    private double avslp;

    public WeatherDataInvalidDatatype() {
    }

    public int getDay() {
        return day;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getAverageTemp() {
        return averageTemp;
    }

    public float getAvdp() {
        return avdp;
    }

    public int getTpcpn() {
        return tpcpn;
    }

    public int getPdir() {
        return pdir;
    }

    public double getAvsp() {
        return avsp;
    }

    public int getDir() {
        return dir;
    }

    public int getMxs() {
        return mxs;
    }

    public double getSkyc() {
        return skyc;
    }

    public int getMxr() {
        return mxr;
    }

    public int getMn() {
        return mn;
    }

    public double getAvslp() {
        return avslp;
    }

    public int getTempSpread() {
        return Math.abs(getMaxTemp() - getMinTemp());
    }
}
