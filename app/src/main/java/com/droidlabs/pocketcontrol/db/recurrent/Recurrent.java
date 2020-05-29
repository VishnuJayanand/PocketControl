package com.droidlabs.pocketcontrol.db.recurrent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recurrents")
public class Recurrent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private Long date;

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
}
