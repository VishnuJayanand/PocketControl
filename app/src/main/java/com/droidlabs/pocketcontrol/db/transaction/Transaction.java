package com.droidlabs.pocketcontrol.db.transaction;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "text_node")
    private String textNote;

    private float amount;

    public Transaction(String textNote, float amount) {
        this.textNote = textNote;
        this.amount = amount;
    }

    public String getTextNote() {
        return this.textNote;
    }

    public float getAmount() {
        return this.amount;
    }

    public int getId() {
        return this.id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }
}
