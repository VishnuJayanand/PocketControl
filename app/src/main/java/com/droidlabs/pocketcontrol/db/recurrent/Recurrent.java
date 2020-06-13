package com.droidlabs.pocketcontrol.db.recurrent;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidlabs.pocketcontrol.db.user.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "recurrents",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "owner_id",
        onDelete = CASCADE
    ),
    indices = {@Index("owner_id")}
)
public class Recurrent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private Long date;

    // Foreign keys
    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    /**
     * Empty constructor.
     */
    public Recurrent() { }

    /**
     * Set id.
     * @param recId id.
     */
    public void setId(final int recId) {
        this.id = recId;
    }

    /**
     * Set date.
     * @param recDate date.
     */
    public void setDate(final Long recDate) {
        this.date = recDate;
    }

    /**
     * Set owner id.
     * @param mOwnerId owner id.
     */
    public void setOwnerId(final @Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    /**
     * Get id.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get date.
     * @return date.
     */
    public Long getDate() {
        return date;
    }

    /**
     * Get owner id.
     * @return owner id.
     */
    @Nullable
    public String getOwnerId() {
        return ownerId;
    }
}
