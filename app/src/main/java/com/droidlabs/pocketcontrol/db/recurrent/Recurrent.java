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

    public Recurrent() { }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Long getDate() {
        return date;
    }
}
