package com.droidlabs.pocketcontrol.db.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "first_name")
    @Nullable
    private String firstName;

    @ColumnInfo(name = "last_name")
    @Nullable
    private String lastName;

    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "access_pin")
    private String accessPin;

    public int getId() {
        return id;
    }

    public String getAccessPin() {
        return accessPin;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccessPin(String accessPin) {
        this.accessPin = accessPin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
