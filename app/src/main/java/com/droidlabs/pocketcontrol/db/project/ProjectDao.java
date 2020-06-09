package com.droidlabs.pocketcontrol.db.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProjectDao {
    /**
     * Insert new project into the database.
     * @param project project to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Project project);

    /**
     * Delete all projects from the database.
     */
    @Query("DELETE FROM projects")
    void deleteAll();

    /**
     * Retrieve all projects from the database.
     * @return all projects.
     */
    @Query("SELECT * FROM projects WHERE is_public=1 OR owner_id=:userId")
    LiveData<List<Project>> getAllProjects(String userId);

    /**
     * Get transaction by id.
     * @param projectId id.
     * @return transaction.
     */
    @Query("SELECT * FROM projects WHERE id=:projectId")
    Project getProjectById(long projectId);
}
