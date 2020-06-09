package com.droidlabs.pocketcontrol.db.project;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidlabs.pocketcontrol.db.user.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "projects",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "owner_id",
                onDelete = CASCADE
        ),
        indices = {@Index("owner_id")}
)
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    @Nullable
    private String color;

    @ColumnInfo(name = "is_public", defaultValue = "0")
    @Nullable
    private Boolean isPublic;

    // Foreign keys
    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    public Project() { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getColor() {
        return color;
    }

    @Nullable
    public String getOwnerId() {
        return ownerId;
    }

    @Nullable
    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setId(int mId) {
        this.id = mId;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public void setColor(@Nullable String mColor) {
        this.color = mColor;
    }

    public void setOwnerId(@Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    public void setPublic(@Nullable Boolean mPublic) {
        this.isPublic = mPublic;
    }
}
