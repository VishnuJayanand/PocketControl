package com.droidlabs.pocketcontrol.db.paymentmode;

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
    tableName = "payment_modes",
    foreignKeys = @ForeignKey(
        entity = Icon.class,
        parentColumns = "id",
        childColumns = "icon",
        onDelete = SET_NULL
    ),
    indices = {@Index("icon")}
)
public class PaymentMode {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    // Foreign keys
    @ColumnInfo(name = "icon")
    @Nullable
    private String icon;

    /**
     * Empty payment mode constructor.
     */
    public PaymentMode() { }

    /**
     * Constructor for payment mode.
     * @param payModeName name of payment mode.
     */
    @Ignore
    public PaymentMode(final String payModeName) {
        this.name = payModeName;
    }

    /**
     * ID getter.
     * @return payment mode id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Name getter.
     * @return payment mode name.
     */
    public String getName() {
        return name;
    }

    /**
     * Icon getter.
     * @return payment mode icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * ID setter.
     * @param payModeId id of payment mode.
     */
    public void setId(final int payModeId) {
        this.id = payModeId;
    }

    /**
     * Name setter.
     * @param payModeName name of payment mode.
     */
    public void setName(final String payModeName) {
        this.name = payModeName;
    }

    /**
     * Icon ID setter.
     * @param iconId id of icon.
     */
    public void setIcon(final @Nullable String iconId) {
        this.icon = iconId;
    }
}
