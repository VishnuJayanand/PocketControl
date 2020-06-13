package com.droidlabs.pocketcontrol.db.category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidlabs.pocketcontrol.db.user.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "categories",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "owner_id",
            onDelete = CASCADE
        ),
    },
    indices = {@Index("owner_id")}
)
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    // Foreign keys
    @ColumnInfo(name = "icon")
    @Nullable
    private int icon;

    // Foreign keys
    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    /**
     * Category constructor.
     * @param categoryId id int
     * @param categoryName name String
     * @param categoryIcon icon int
     */
    public Category(final int categoryId, final String categoryName, final int categoryIcon) {
        this.id = categoryId;
        this.name = categoryName;
        this.icon = categoryIcon;
    }

    /**
     * Empty category constructor.
     */
    public Category() { }

    /**
     * Category constructor.
     * @param categoryName category name.
     */
    @Ignore
    public Category(final String categoryName) {
        this.name = categoryName;
    }

    /**
     * Category constructor.
     * @param categoryName category name.
     * @param iconName icon name.
     */
    @Ignore
    public Category(final String categoryName, final int iconName) {
        this.name = categoryName;
        this.icon = iconName;
    }

    /**
     * ID getter.
     * @return category id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Name getter.
     * @return category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Icon getter.
     * @return icon id.
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Get category owner id.
     * @return owner id.
     */
    @Nullable
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Category id setter.
     * @param categoryId category id.
     */
    public void setId(final int categoryId) {
        this.id = categoryId;
    }

    /**
     * Category name setter.
     * @param catName category name.
     */
    public void setName(final String catName) {
        this.name = catName;
    }

    /**
     * Category icon setter.
     * @param categoryIcon category icon id.
     */
    public void setIcon(final int categoryIcon) {
        this.icon = categoryIcon;
    }

    /**
     * Set category owner id.
     * @param mOwnerId owner id.
     */
    public void setOwnerId(final @Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    /**
     * Override toString class method to return category name.
     * @return category name
     */
    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
