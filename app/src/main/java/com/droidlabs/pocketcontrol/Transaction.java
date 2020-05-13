package com.droidlabs.pocketcontrol;

import java.util.Date;

public class Transaction {
    private int id;
    private String title;
    private Date date;
    private String type;
    private float amount;
    private String note;
    private String currency;
    private int categoryImage;
    private String categoryTitle;

    /**
     * Constructor for the Transaction class.
     * @param id int
     * @param title String
     * @param date date can be turn to String when input in TextView
     * @param type String
     * @param amount float
     * @param note String
     * @param currency String
     * @param categoryImage int
     * @param categoryTitle String
     */
    public Transaction(final int id, final String title, final Date date, final String type, final float amount, final String note, final String currency, final int categoryImage, final String categoryTitle) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.currency = currency;
        this.categoryImage = categoryImage;
        this.categoryTitle = categoryTitle;
    }

    /**
     * Getter for Currency.
     * @return String
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Setter for Currency.
     * @param currency String
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Getter for CategoryImage.
     * @return int
     */
    public int getCategoryImage() {
        return categoryImage;
    }

    /**
     * Setter for Catergory Image.
     * @param categoryImage int
     */
    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    /**
     * Getter for CategoryTitle.
     * @return String
     */
    public String getCategoryTitle() {
        return categoryTitle;
    }

    /**
     * Setter for CategoryTitle
     * @param categoryTitle String
     */
    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    /**
     * Getter for title.
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for ID.
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for ID.
     * @param date date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter for Type.
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter for Amount.
     * @param amount float
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * Setter for Note.
     * @param note String
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Getter for ID.
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for Date.
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for Type.
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for Amount.
     * @return float
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Getter for Note.
     * @return note
     */
    public String getNote() {
        return note;
    }
}
