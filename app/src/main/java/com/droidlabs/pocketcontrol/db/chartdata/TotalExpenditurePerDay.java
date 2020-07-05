package com.droidlabs.pocketcontrol.db.chartdata;

public class TotalExpenditurePerDay {

    private long date;
    private float totalExpenditure;

    /**
     * Constructor.
     */
    public TotalExpenditurePerDay() {
    }

    /**
     * Date setter.
     * @param mDate date
     */
    public void setDate(final long mDate) {
        this.date = mDate;
    }

    /**
     * Total expenditure setter.
     * @param totalExp date
     */
    public void setTotalExpenditure(final float totalExp) {
        this.totalExpenditure = totalExp;
    }

    /**
     * Total expenditure getter.
     * @return total expenditure.
     */
    public float getTotalExpenditure() {
        return totalExpenditure;
    }

    /**
     * Date getter.
     * @return date.
     */
    public long getDate() {
        return date;
    }
}
