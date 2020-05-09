package com.droidlabs.pocketcontrol.db.currency;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDao {
    /**
     * Insert new currency.
     * @param currency currency to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Currency currency);

    /**
     * Delete all currencies.
     */
    @Query("DELETE FROM currencies")
    void deleteAll();

    /**
     * Retrieve all currencies.
     * @return list of currencies.
     */
    @Query("SELECT * FROM currencies")
    LiveData<List<Currency>> getAllCurrencies();
}
