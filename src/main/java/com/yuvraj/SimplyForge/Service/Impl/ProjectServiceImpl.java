package com.yuvraj.SimplyForge.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.ProjectService;
import com.yuvraj.SimplyForge.dto.project.ProjectRequest;
import com.yuvraj.SimplyForge.dto.project.ProjectResponse;
import com.yuvraj.SimplyForge.dto.project.ProjectSummaryResponse;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserProjects'");
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserProjectsById'");
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProject'");
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProject'");
    }

    @Override
    public void softDelete(Long id, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'softDelete'");
    }

}
