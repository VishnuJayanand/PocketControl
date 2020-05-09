package com.droidlabs.pocketcontrol.db.icon;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "icons")
public class Icon {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    @Nullable
    private String color;

    /**
     * Empty icon constructor.
     */
    public Icon() { }

    /**
     * Icon constructor.
     * @param iconName name of the icon.
     */
    @Ignore
    public Icon(final String iconName) {
        this.name = iconName;
    }

    /**
     * ID getter.
     * @return icon id.
     */
    public int getId() {
        return id;
    }

    /**
     * Icon color getter.
     * @return icon color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Icon name getter.
     * @return icon name.
     */
    public String getName() {
        return name;
    }

    /**
     * Icon ID setter.
     * @param iconId icon id.
     */
    public void setId(final int iconId) {
        this.id = iconId;
    }

    /**
     * Icon color setter.
     * @param iconColor icon color.
     */
    public void setColor(final @Nullable String iconColor) {
        this.color = iconColor;
    }

    /**
     * Icon name setter.
     * @param iconName icon name.
     */
    public void setName(final String iconName) {
        this.name = iconName;
    }
}
