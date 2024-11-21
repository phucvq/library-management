package librarymanagement.service;

import librarymanagement.entity.Member;
import librarymanagement.repository.MemberMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public List<Member> getMembersByFilters(String name, String membershipType, Boolean active) {
        return memberMapper.getMembersByFilters(name, membershipType, active);
    }

    public List<Member> findByStatusOrType(Boolean status, String membershipType) {
        return memberMapper.getMembersByStatusOrType(status, membershipType);
    }

    public List<Member> findByMembersByTypes(List<String> types) {
        return memberMapper.getMembersByTypes(types);
    }

    public List<Map<String, Object>> findMemberCountByType() {
        return memberMapper.getMemberCountByType();
    }

    public  List<Member> findMembersByActivityStatus(boolean status) {
        return memberMapper.getMembersByActivityStatus(status);
    }

    public List<Member> findMembersJoinedAfter(LocalDate date) {
        return memberMapper.getMembersJoinedAfter(date);
    }

    public void updateMembershipTypeIfBlank(String newType) {
        memberMapper.updateMembershipTypeIfBlank(newType);
    }

    public void deleteMemberBeforeDate(LocalDate date) {
        memberMapper.deleteMembersBeforeDate(date);
    }

    public Member addNewMember(Member member) {
        memberMapper.insertMember(member);
        return member;
    }

    public List<Member> findAllMembers() {
        return memberMapper.getAllMembers();
    }
}

