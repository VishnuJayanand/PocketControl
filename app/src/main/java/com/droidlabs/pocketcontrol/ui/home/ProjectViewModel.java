package com.droidlabs.pocketcontrol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.project.Project;
import com.droidlabs.pocketcontrol.db.project.ProjectRepository;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    private ProjectRepository projectRepository;
    private LiveData<List<Project>> projects;

    public ProjectViewModel(final Application application) {
        super(application);

        this.projectRepository = new ProjectRepository(application);
    }

    public LiveData<List<Project>> getProjects() {
        return projectRepository.getAllProjects();
    }
}
