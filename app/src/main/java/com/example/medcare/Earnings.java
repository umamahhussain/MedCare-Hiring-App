package com.example.medcare;

public class Earnings {
    private String medicId;
    private long durationMillis;
    private double amountEarned;
    private long timestamp;

    Earnings(){

    }
    public Earnings(String medicId, long durationMillis, double amountEarned, long timestamp) {
        this.medicId = medicId;
        this.durationMillis = durationMillis;
        this.amountEarned = amountEarned;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMedicId() {
        return medicId;
    }

    public void setMedicId(String medicId) {
        this.medicId = medicId;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public double getAmountEarned() {
        return amountEarned;
    }

    public void setAmountEarned(double amountEarned) {
        this.amountEarned = amountEarned;
    }
}
