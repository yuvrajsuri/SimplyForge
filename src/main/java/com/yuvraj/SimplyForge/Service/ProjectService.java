package com.yuvraj.SimplyForge.Service;

import java.util.List;

import com.yuvraj.SimplyForge.dto.project.ProjectRequest;
import com.yuvraj.SimplyForge.dto.project.ProjectResponse;
import com.yuvraj.SimplyForge.dto.project.ProjectSummaryResponse;

public interface ProjectService {

    public List<ProjectSummaryResponse> getUserProjects(Long userId);

    public ProjectResponse getUserProjectsById(Long id, Long userId);

    public ProjectResponse createProject(ProjectRequest request, Long userId);

    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    public void softDelete(Long id, Long userId);

}
