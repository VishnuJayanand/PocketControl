package com.droidlabs.pocketcontrol.db.category;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidlabs.pocketcontrol.db.icon.Icon;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(
    tableName = "categories",
    foreignKeys = @ForeignKey(
        entity = Icon.class,
        parentColumns = "id",
        childColumns = "icon",
        onDelete = SET_NULL
    ),
    indices = {@Index("icon")}
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

    /**
     * Full category constructor.
     * @param id category id
     * @param name category name
     * @param icon category icon
     */
    public Category(final int id, final String name, final int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /**
     * Empty category constructor.
     */
    @Ignore
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
    public void setIcon(final @Nullable  int categoryIcon) {
        this.icon = categoryIcon;
    }
}
