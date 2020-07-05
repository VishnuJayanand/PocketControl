package com.droidlabs.pocketcontrol.db.chartdata;

public class TotalIncomePerDay {
    private long date;
    private float totalIncome;

    /**
     * Constructor.
     */
    public TotalIncomePerDay() {
    }

    /**
     * Date setter.
     * @param mDate date.
     */
    public void setDate(final long mDate) {
        this.date = mDate;
    }

    /**
     * Total income setter.
     * @param totalInc total income.
     */
    public void setTotalIncome(final float totalInc) {
        this.totalIncome = totalInc;
    }

    /**
     * Total income getter.
     * @return total income.
     */
    public float getTotalIncome() {
        return totalIncome;
    }

    /**
     * Date getter.
     * @return date.
     */
    public long getDate() {
        return date;
    }
}
