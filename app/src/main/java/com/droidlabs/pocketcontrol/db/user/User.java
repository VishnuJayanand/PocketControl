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

    /**
     * Get user id.
     * @return user id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get user access pin.
     * @return access pin.
     */
    public String getAccessPin() {
        return accessPin;
    }

    /**
     * Get user email.
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get user first name.
     * @return first name.
     */
    @Nullable
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get user last name.
     * @return last name.
     */
    @Nullable
    public String getLastName() {
        return lastName;
    }

    /**
     * Get user password.
     * @return password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set user id.
     * @param mId id.
     */
    public void setId(final int mId) {
        this.id = mId;
    }

    /**
     * Set user access pin.
     * @param mAccessPin access pin.
     */
    public void setAccessPin(final String mAccessPin) {
        this.accessPin = mAccessPin;
    }

    /**
     * Set user email.
     * @param mEmail email.
     */
    public void setEmail(final String mEmail) {
        this.email = mEmail;
    }

    /**
     * Set user first name.
     * @param fn name.
     */
    public void setFirstName(final @Nullable String fn) {
        this.firstName = fn;
    }

    /**
     * Set user last name.
     * @param ln last name.
     */
    public void setLastName(final @Nullable String ln) {
        this.lastName = ln;
    }

    /**
     * Set user password.
     * @param pwd password.
     */
    public void setPassword(final String pwd) {
        this.password = pwd;
    }
}
