package de.exxcellent.challenge.data;

import de.exxcellent.challenge.CsvProperty;

public class WeatherData {
//    Day,MxT,MnT,AvT,AvDP,1HrP TPcpn,PDir,AvSp,Dir,MxS,SkyC,MxR,Mn,R AvSLP
//     1 ,88 ,59 ,74 ,53.8,0         ,280 ,9.6 ,270,17 ,1.6 ,93 ,23,1004.5

    @CsvProperty("Day")
    private int day;
    @CsvProperty("MxT")
    private int maxTemp;
    @CsvProperty("MnT")
    private int minTemp;
    @CsvProperty("AvT")
    private int averageTemp;
    @CsvProperty("AvDP")
    private double avdp;
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

    public WeatherData() {
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

    public double getAvdp() {
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
