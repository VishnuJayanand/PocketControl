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
import com.droidlabs.pocketcontrol.db.project.Project;
import com.droidlabs.pocketcontrol.db.user.User;


import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(
    tableName = "transactions",
    foreignKeys = {
            @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category",
                onDelete = SET_NULL
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "id",
                    childColumns = "owner_id",
                    onDelete = CASCADE
            ),
            @ForeignKey(
                    entity = Project.class,
                    parentColumns = "id",
                    childColumns = "project",
                    onDelete = SET_NULL
            ),
    },
    indices = {@Index("category"), @Index("owner_id"), @Index("project")}
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

    @ColumnInfo(name = "flag_icon_recurring", defaultValue = "0")
    @Nullable
    private Boolean flagIconRecurring;

    @ColumnInfo(name = "recurring_interval_type", defaultValue = "0")
    @Nullable
    private Integer recurringIntervalType;

    @ColumnInfo(name = "recurring_interval_days", defaultValue = "0")
    @Nullable
    private Integer recurringIntervalDays;

    // 1 - Expense, 2 - Income
    @ColumnInfo(name = "type", defaultValue = "1")
    private int type;

    // 1 - Cash, 2 - Card
    @ColumnInfo(name = "method", defaultValue = "1")
    private int method;

    @ColumnInfo(name = "date")
    private Long date;

    // Foreign keys
    @ColumnInfo(name = "category")
    @Nullable
    private String category;

    @ColumnInfo(name = "project")
    @Nullable
    private String project;

    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    /**
     * Transaction constructor with amount, type, category and date.
     * @param transactionAmount float transaction amount
     * @param transactionType int transaction type
     * @param transactionCategory String transaction String
     * @param transactionDate Date transaction date
     * @param transactionNote String transaction note
     * @param transactionMethod int transaction method
     */
    public Transaction(
            final Float transactionAmount,
            final Integer transactionType,
            final @Nullable String transactionCategory,
            final Long transactionDate,
            final @Nullable String transactionNote,
            final Integer transactionMethod
    ) {
        this.amount = transactionAmount;
        this.type = transactionType;
        this.category = transactionCategory;
        this.date = transactionDate;
        this.textNote = transactionNote;
        this.method = transactionMethod;
    }

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
     * Getter for icon recurring flag.
     * @return flag.
     */
    @Nullable
    public Boolean getFlagIconRecurring() {
        return flagIconRecurring;
    }

    /**
     * Getter for recurring type.
     * 1 - Daily
     * 2 - Weekly
     * 3 - Monthly
     * 4 - Custom
     * @return recurring type.
     */
    @Nullable
    public Integer getRecurringIntervalType() {
        return recurringIntervalType;
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

    @Nullable
    public String getOwnerId() {
        return ownerId;
    }

    @Nullable
    public String getProject() {
        return project;
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
     * Setter for recurring icon flag.
     * @param flag flag.
     */
    public void setFlagIconRecurring(final @Nullable Boolean flag) {
        this.flagIconRecurring = flag;
    }

    /**
     * Setter for recurring type.
     * 1 - Daily
     * 2 - Weekly
     * 3 - Monthly
     * 4 - Custom
     * @param recType type.
     */
    public void setRecurringIntervalType(final @Nullable Integer recType) {
        this.recurringIntervalType = recType;
    }

    /**
     * Recurring interval setter.
     * @param recIntDays recurring day interval.
     */
    public void setRecurringIntervalDays(final @Nullable Integer recIntDays) {
        this.recurringIntervalDays = recIntDays;
    }

    public void setOwnerId(@Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    public void setProject(@Nullable String mProject) {
        this.project = mProject;
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

    /**
     * Date  getter.
     * @return transaction date
     */
    public Long getDate() {
        return date;
    }

    /**
     * Date setter.
     * @param tDate transaction date
     */
    public void setDate(final Long tDate) {
        this.date = tDate;
    }

    /**
     * Method getter.
     * @return int transaction method
     */
    public Integer getMethod() {
        return method; }

    /**
     * Method setter.
     * @param paymentMethod int transaction method
     */
    public void setMethod(final Integer paymentMethod) {
        this.method = paymentMethod; }
}
