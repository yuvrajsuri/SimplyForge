package com.yuvraj.SimplyForge.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.ProjectMemberService;
import com.yuvraj.SimplyForge.dto.member.InviteMemberRequest;
import com.yuvraj.SimplyForge.dto.member.MemberResponse;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService{

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProjectMembers'");
    }

    @Override
    public MemberResponse invitedMember(long projectId, InviteMemberRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invitedMember'");
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemberRole'");
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProjectMember'");
    }

}
