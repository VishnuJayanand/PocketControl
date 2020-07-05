package com.droidlabs.pocketcontrol.ui.home;

public class InfoTipHelperClass {

    private String title, desc;

    /**
     * featured helper class.
     * @param sdesc description
     * @param sTitle title
     */
    public InfoTipHelperClass(final String sTitle, final String sdesc) {
        this.title = sTitle;
        this.desc = sdesc;
    }

    /**
     * Getter method for title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for desc.
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

}
