package com.yuvraj.SimplyForge.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.FileService;
import com.yuvraj.SimplyForge.dto.project.FileContentResponse;
import com.yuvraj.SimplyForge.dto.project.FileNode;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileTree'");
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileContent'");
    }

}
