package com.droidlabs.pocketcontrol.db.transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidlabs.pocketcontrol.db.category.Category;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(
    tableName = "transactions",
    foreignKeys = @ForeignKey(
        entity = Category.class,
        parentColumns = "id",
        childColumns = "category",
        onDelete = SET_NULL
    ),
    indices = {@Index("category")}
)
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "amount")
    private Float amount;

    @ColumnInfo(name = "text_node", defaultValue = "")
    @Nullable
    private String textNote;

    @ColumnInfo(name = "is_recurring", defaultValue = "0")
    @Nullable
    private Boolean isRecurring;

    @ColumnInfo(name = "recurring_interval_days", defaultValue = "0")
    @Nullable
    private Integer recurringIntervalDays;

    // 1 - Expense, 2 - Income
    @ColumnInfo(name = "type", defaultValue = "1")
    private Integer type;

    // Foreign keys
    @ColumnInfo(name = "category")
    @Nullable
    private String category;

    /**
     * Empty transaction constructor.
     */
    public Transaction() { }

    /**
     * Transaction constructor.
     * @param transAmount transaction amount.
     */
    @Ignore
    public Transaction(final float transAmount) {
        this.amount = transAmount;
    }

    /**
     * Transaction constructor with note.
     * @param transAmount transaction amount.
     * @param note transaction note.
     */
    @Ignore
    public Transaction(final float transAmount, final @Nullable String note) {
        this.amount = transAmount;
        this.textNote = note;
    }

    /**
     * Transaction constructor with note and category.
     * @param transAmount transaction amount.
     * @param note transaction note.
     * @param categoryId category id.
     */
    @Ignore
    public Transaction(final float transAmount, final @Nullable String note, final @Nullable String categoryId) {
        this.textNote = note;
        this.amount = transAmount;
        this.category = categoryId;
    }

    /**
     * Transaction constructor with note and recurring.
     * @param transAmount transaction amount.
     * @param note transaction note.
     * @param recurring whether transaction is recurring.
     * @param recIntDays interval in days of recurring.
     * @param transType transaction type.
     */
    @Ignore
    public Transaction(
            final float transAmount,
            final @Nullable String note,
            final @Nullable Boolean recurring,
            final int transType,
            final @Nullable Integer recIntDays
    ) {
        this.textNote = note;
        this.amount = transAmount;
        this.isRecurring = recurring;
        this.recurringIntervalDays = recIntDays;
        this.type = transType;
    }

    /**
     * ID getter.
     * @return transaction id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Amount getter.
     * @return transaction amount.
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * Note getter.
     * @return transaction text note.
     */
    public String getTextNote() {
        return textNote;
    }

    /**
     * Recurring getter.
     * @return whether transaction is recurring.
     */
    public Boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Recurring days getter.
     * @return interval for recurring transaction.
     */
    public Integer getRecurringIntervalDays() {
        return recurringIntervalDays;
    }

    /**
     * Type getter.
     * @return transaction type.
     */
    public Integer getType() {
        return type;
    }

    /**
     * Category getter.
     * @return transaction category id.
     */
    public String getCategory() {
        return category;
    }

    /**
     * ID setter.
     * @param transId transaction id.
     */
    public void setId(final int transId) {
        this.id = transId;
    }

    /**
     * Amount setter.
     * @param transAmount transaction amount.
     */
    public void setAmount(final Float transAmount) {
        this.amount = transAmount;
    }

    /**
     * Note setter.
     * @param note transaction text note.
     */
    public void setTextNote(final @Nullable String note) {
        this.textNote = note;
    }

    /**
     * Recurring setter.
     * @param recurring whether transaction is recurring.
     */
    public void setRecurring(final Boolean recurring) {
        isRecurring = recurring;
    }

    /**
     * Recurring interval setter.
     * @param recIntDays recurring day interval.
     */
    public void setRecurringIntervalDays(final @Nullable Integer recIntDays) {
        this.recurringIntervalDays = recIntDays;
    }

    /**
     * Type setter.
     * @param transType transaction type.
     */
    public void setType(final Integer transType) {
        this.type = transType;
    }

    /**
     * Category ID setter.
     * @param categoryId category id.
     */
    public void setCategory(final @Nullable String categoryId) {
        this.category = categoryId;
    }
}
