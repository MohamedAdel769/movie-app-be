package com.stp.app.dto;

public class Rate {
    private Double rate;
    private String description;

    public Rate(Double rate, String description) {
        this.rate = rate;
        this.description = description;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "rate=" + rate +
                ", description='" + description + '\'' +
                '}';
    }
}
