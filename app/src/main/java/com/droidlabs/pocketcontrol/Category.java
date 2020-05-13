package com.droidlabs.pocketcontrol;

public class Category {
    private int mImageId;
    private String mTitle;

    /**
     * constructor for Catergory.
     * @param imageId int the image id of category
     * @param title string the title of category
     */
    public Category(final int imageId, final String title) {
        this.mImageId = imageId;
        this.mTitle = title;
    }

    /**
     * getter method.
     * @return return the image ID
     */
    public int getImageId() {
        return mImageId;
    }

    /**
     * setter method.
     * @param imageId int the imageID
     */
    public void setImageId(final int imageId) {
        this.mImageId = imageId;
    }

    /**
     * getter method.
     * @return string return the title of parameter
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * setter method.
     * @param title string the title of category
     */
    public void setTitle(final String title) {
        this.mTitle = title;
    }

}
