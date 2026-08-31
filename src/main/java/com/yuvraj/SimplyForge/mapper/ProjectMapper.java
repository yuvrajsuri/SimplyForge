package com.yuvraj.SimplyForge.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yuvraj.SimplyForge.Entity.Project;
import com.yuvraj.SimplyForge.dto.project.ProjectResponse;
import com.yuvraj.SimplyForge.dto.project.ProjectSummaryResponse;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "projectName", source = "name")
    ProjectSummaryResponse toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);

}
