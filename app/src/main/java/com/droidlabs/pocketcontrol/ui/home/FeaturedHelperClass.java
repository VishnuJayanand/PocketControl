package com.droidlabs.pocketcontrol.ui.home;

public class FeaturedHelperClass {

    private String title, category, amount;
    private int image;

    /**
     * featured helper class.
     * @param sAmount amount
     * @param sCategory cateogry
     * @param sImage image
     * @param sTitle title
     */
    public FeaturedHelperClass(final String sTitle, final String sCategory, final String sAmount, final int sImage) {
        this.title = sTitle;
        this.category = sCategory;
        this.amount = sAmount;
        this.image = sImage;
    }

    /**
     * Getter method for title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for category.
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter method for amount.
     * @return amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Getter method for image.
     * @return image
     */
    public int getImage() {
        return image;
    }
}
