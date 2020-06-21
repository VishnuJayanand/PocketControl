package com.droidlabs.pocketcontrol.db.currency;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "currencies")
public class Currency {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "three_letter_code", defaultValue = "")
    private String threeLetterCode;

    @ColumnInfo(name = "is_default", defaultValue = "NULL")
    @Nullable
    private Boolean isDefault;

    @ColumnInfo(name = "to_default_rate", defaultValue = "0")
    @Nullable
    private Float toDefaultRate;

    @ColumnInfo(name = "symbol", defaultValue = "0")
    @Nullable
    private String symbol;

    /**
     * Empty currency constructor.
     */
    public Currency() { }

    /**
     * Currency constructor.
     * @param letterCode three-letter code.
     * @param currencySymbol symbol for the currency.
     */
    @Ignore
    public Currency(final String letterCode, final String currencySymbol) {
        this.threeLetterCode = letterCode;
        this.symbol = currencySymbol;
    }

    /**
     * Currency constructor with default flag.
     * @param letterCode three-letter code.
     * @param isDef whether currency is default.
     */
    @Ignore
    public Currency(final String letterCode, final @Nullable Boolean isDef) {
        this.threeLetterCode = letterCode;
        this.isDefault = isDef;
    }

    /**
     * Complete currency constructor.
     * @param letterCode three-letter code.
     * @param isDef whether currency is default.
     * @param rate conversion rate to default currency.
     */
    @Ignore
    public Currency(final String letterCode, final @Nullable Boolean isDef, final @Nullable Float rate) {
        this.threeLetterCode = letterCode;
        this.isDefault = isDef;
        this.toDefaultRate = rate;
    }

    /**
     * ID getter.
     * @return currency id.
     */
    public int getId() {
        return id;
    }

    /**
     * symbol getter.
     * @return symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * ToDefaultRate getter.
     * @return conversion rate to default currency.
     */
    public Float getToDefaultRate() {
        return toDefaultRate;
    }

    /**
     * Three-letter code getter.
     * @return three-letter currency code.
     */
    public String getThreeLetterCode() {
        return threeLetterCode;
    }

    /**
     * IsDefault getter.
     * @return whether the currency is the default currency or not.
     */
    public Boolean isDefault() {
        return isDefault;
    }

    /**
     * ID setter.
     * @param currencyId currency id.
     */
    public void setId(final int currencyId) {
        this.id = currencyId;
    }

    /**
     * ToDefaultRate setter.
     * @param rate conversion rate between currency and default currency.
     */
    public void setToDefaultRate(final @Nullable Float rate) {
        this.toDefaultRate = rate;
    }

    /**
     * Three-letter code setter.
     * @param letterCode three-letter code of the currency.
     */
    public void setThreeLetterCode(final String letterCode) {
        this.threeLetterCode = letterCode;
    }

    /**
     * currency symbol setter.
     * @param currencySymbol three-letter code of the currency.
     */
    public void setSymbol(final String currencySymbol) {
        this.symbol = currencySymbol;
    }

    /**
     * IsDefault setter.
     * @param isDef set currency as default.
     */
    public void setDefault(final boolean isDef) {
        this.isDefault = isDef;
    }
}
