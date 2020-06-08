package com.droidlabs.pocketcontrol.db.budget;

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
    tableName = "budgets",
    foreignKeys = @ForeignKey(
        entity = Category.class,
        parentColumns = "id",
        childColumns = "category",
        onDelete = SET_NULL
    ),
    indices = {@Index("category")}
)
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "max_amount", defaultValue = "0")
    private Float maxAmount;

    @ColumnInfo(name = "description", defaultValue = "")
    @Nullable
    private String description;

    @ColumnInfo(name = "is_global", defaultValue = "NULL")
    @Nullable
    private Boolean isGlobal;

    // Foreign keys
    @ColumnInfo(name = "category")
    @Nullable
    private String category;

    /**
     * Empty budget constructor.
     */
    public Budget() { }

    /**
     * Create a budget with only a amount.
     * @param amount maximum budget amount.
     */
    @Ignore
    public Budget(final Float amount) {
        this.maxAmount = amount;
    }

    /**
     * Create a budget with amount and description.
     * @param amount maximum budget amount.
     * @param desc budget description.
     */
    @Ignore
    public Budget(final Float amount, final @Nullable String desc) {
        this.maxAmount = amount;
        this.description = desc;
    }

    /**
     * Create a complete budget object.
     * @param amount maximum budget amount.
     * @param desc budget description.
     * @param global whether the budget is global.
     */
    @Ignore
    public Budget(final Float amount, final @Nullable String desc, final @Nullable Boolean global) {
        this.maxAmount = amount;
        this.description = desc;
        this.isGlobal = global;
    }

    /**
     * ID getter.
     * @return id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Max amount getter.
     * @return max amount.
     */
    public Float getMaxAmount() {
        return this.maxAmount;
    }

    /**
     * Description getter.
     * @return description.
     */
    public @Nullable String getDescription() {
        return this.description;
    }

    /**
     * isGlobal getter.
     * @return whether budget is global or not.
     */
    public @Nullable Boolean isGlobal() {
        return this.isGlobal;
    }

    /**
     * Category getter.
     * @return category id.
     */
    public @Nullable String getCategory() {
        return this.category;
    }

    /**
     * ID setter.
     * @param budgetId budget id.
     */
    public void setId(final int budgetId) {
        this.id = budgetId;
    }

    /**
     * Max amount setter.
     * @param amount maximum budget amount.
     */
    public void setMaxAmount(final Float amount) {
        this.maxAmount = amount;
    }

    /**
     * Description setter.
     * @param desc budget description.
     */
    public void setDescription(final @Nullable String desc) {
        this.description = desc;
    }

    /**
     * IsGlobal setter.
     * @param global whether budget is global.
     */
    public void setGlobal(final Boolean global) {
        this.isGlobal = global;
    }

    /**
     * Category setter.
     * @param cat category id.
     */
    public void setCategory(final @Nullable String cat) {
        this.category = cat;
    }
}