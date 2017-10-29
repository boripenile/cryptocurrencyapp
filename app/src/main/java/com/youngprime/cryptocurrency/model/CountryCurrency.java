package com.youngprime.cryptocurrency.model;

/**
 * Created by bd_dev_murtala on 19/10/2017.
 */

public class CountryCurrency {
    private String flagUrl;

    private String currencyName;

    private String symbol;

    private String code;

    public CountryCurrency(String flagUrl, String currencyName, String symbol, String code) {
        this.flagUrl = flagUrl;
        this.currencyName = currencyName;
        this.symbol = symbol;
        this.code = code;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
