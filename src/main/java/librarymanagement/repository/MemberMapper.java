package librarymanagement.repository;

import librarymanagement.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    List<Member> getMembersByFilters(
            @Param("name") String name,
            @Param("membershipType") String membershipType,
            @Param("active") Boolean active
    );


    List<Member> getMembersByStatusOrType(@Param("status") Boolean status,
                                          @Param("membershipType") String membershipType);

    List<Member> getMembersByTypes(@Param("types") List<String> types);

    List<Map<String, Object>> getMemberCountByType();
    List<Member> getMembersByActivityStatus(@Param("status") boolean status);
    List<Member> getMembersJoinedAfter(@Param("date") LocalDate date);
    void updateMembershipTypeIfBlank(@Param("newType") String newType);
    void deleteMembersBeforeDate(@Param("date") LocalDate date);
    void insertMember(Member member);
    List<Member> getAllMembers();
}

