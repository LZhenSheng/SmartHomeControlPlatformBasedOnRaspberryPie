package com.example.myapplication.domain;

public class DHT {
    private String temperation;
    private String humdity;

    @Override
    public String toString() {
        return "DHT{" +
                "temperation='" + temperation + '\'' +
                ", humdity='" + humdity + '\'' +
                '}';
    }

    public String getTemperation() {
        return temperation;
    }

    public void setTemperation(String temperation) {
        this.temperation = temperation;
    }

    public String getHumdity() {
        return humdity;
    }

    public void setHumdity(String humdity) {
        this.humdity = humdity;
    }
}
