package com.yuvraj.SimplyForge.Service;

import java.util.List;

import com.yuvraj.SimplyForge.dto.member.InviteMemberRequest;
import com.yuvraj.SimplyForge.dto.member.MemberResponse;

public interface ProjectMemberService {

    public List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    public MemberResponse invitedMember(long projectId, InviteMemberRequest request, Long userId);

    public MemberResponse  updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId);

    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);

}
