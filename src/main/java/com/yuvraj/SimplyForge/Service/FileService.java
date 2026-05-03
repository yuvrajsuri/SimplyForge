package com.yuvraj.SimplyForge.Service;

import java.util.List;

import com.yuvraj.SimplyForge.dto.project.FileContentResponse;
import com.yuvraj.SimplyForge.dto.project.FileNode;

public interface FileService {

    public List<FileNode> getFileTree(Long projectId, Long userId);

    public FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
