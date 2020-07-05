package com.droidlabs.pocketcontrol.db.chartdata;

public class TotalExpenditurePerCategory {
    private String categoryId;
    private float categoryAmount;

    /**
     * Constructor.
     */
    public TotalExpenditurePerCategory() { }

    /**
     * Amount setter.
     * @param catAmount amount.
     */
    public void setCategoryAmount(final float catAmount) {
        this.categoryAmount = catAmount;
    }

    /**
     * ID setter.
     * @param catId id.
     */
    public void setCategoryId(final String catId) {
        this.categoryId = catId;
    }

    /**
     * Category amount getter.
     * @return category sum.
     */
    public float getCategoryAmount() {
        return categoryAmount;
    }

    /**
     * Category id getter.
     * @return category id.
     */
    public String getCategoryId() {
        return categoryId;
    }
}
