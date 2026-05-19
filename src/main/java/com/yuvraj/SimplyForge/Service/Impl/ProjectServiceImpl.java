package com.yuvraj.SimplyForge.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Entity.Project;
import com.yuvraj.SimplyForge.Entity.User;
import com.yuvraj.SimplyForge.Service.ProjectService;
import com.yuvraj.SimplyForge.dto.project.ProjectRequest;
import com.yuvraj.SimplyForge.dto.project.ProjectResponse;
import com.yuvraj.SimplyForge.dto.project.ProjectSummaryResponse;
import com.yuvraj.SimplyForge.mapper.ProjectMapper;
import com.yuvraj.SimplyForge.repository.ProjectRepository;
import com.yuvraj.SimplyForge.repository.UserRepository;

@Service
public class ProjectServiceImpl implements ProjectService{

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {

        User owner = userRepository.findById(userId).orElseThrow();

        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .isPublic(false)
                .build();

        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {

//        return projectRepository.findAllAccessibleByUser(userId)
//                .stream()
//                .map(projectMapper::toProjectSummaryResponse)
//                .collect(Collectors.toList());

        var projects = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserProjectsById'");
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
