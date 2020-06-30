package com.droidlabs.pocketcontrol.db.defaults;

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
    tableName = "defaults",
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
public class Defaults {

    @PrimaryKey(autoGenerate = true)
    private int defaultId;

    @ColumnInfo (name = "default_entity", defaultValue = "NULL")
    @Nullable
    private String defaultEntity;

    @ColumnInfo(name = "default_value", defaultValue = "NULL")
    @Nullable
    private String defaultValue;

    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    /**
     * Empty defaults constructor.
     */
    public Defaults() { }

    /**
     * Default constructor.
     * @param entity name String
     * @param value String
     */
    @Ignore
    public Defaults(@Nullable final String entity, @Nullable final String value) {
        this.defaultEntity = entity;
        this.defaultValue = value;
    }
    /**
     * ID getter.
     * @return id default.
     */
    public int getDefaultId() {
        return defaultId;
    }
    /**
     * ID setter.
     * @param id set.
     */
    public void setDefaultId(final int id) {
        this.defaultId = id;
    }
    /**
     * Entity getter.
     * @return entity returned.
     */
    @Nullable
    public String getDefaultEntity() {
        return defaultEntity;
    }
    /**
     * entity setter.
     * @param entity set.
     */
    public void setDefaultEntity(@Nullable final String entity) {
        this.defaultEntity = entity;
    }
    /**
     * value getter.
     * @return value of the corresponding default entity returned.
     */
    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }
    /**
     * value setter.
     * @param value of the default entity set.
     */
    public void setDefaultValue(@Nullable final String value) {
        this.defaultValue = value;
    }

    /**
     * Get owner id.
     * @return owner id.
     */
    @Nullable
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Set owner id.
     * @param mOwnerId owner id.
     */
    public void setOwnerId(final @Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    /**
     * Parses class info to export csv format.
     * @return formatted string.
     */
    public String toExportString() {
        return this.defaultId
                + ", " + this.defaultEntity
                + ", " + this.defaultValue;
    }
}
