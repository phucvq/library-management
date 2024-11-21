package librarymanagement.controller;

import librarymanagement.entity.Member;
import librarymanagement.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    @Autowired
    MemberService memberService;

    @GetMapping("/all-members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok().body(members);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<Member>> getMembersByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String membershipType,
            @RequestParam(required = false) Boolean active
    ) {
        List<Member> members = memberService.getMembersByFilters(name, membershipType, active);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Member>> getMembersByStatus(
            @RequestParam(required = false) boolean status,
            @RequestParam(required = false) String type
    ) {
        List<Member> members = memberService.findByStatusOrType(status, type);
        return ResponseEntity.ok().body(members);
    }

    @GetMapping("/by-types")
    public ResponseEntity<List<Member>> getMembersByTypes(@RequestParam List<String> membershipTypes) {
        List<Member> members = memberService.findByMembersByTypes(membershipTypes);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/count-types")
    public ResponseEntity<List<Map<String, Object>>> countMembersByTypes() {
        List<Map<String, Object>> map = memberService.findMemberCountByType();
        return ResponseEntity.ok(map);
    }

    @GetMapping("/activity-status/{status}")
    public ResponseEntity<List<Member>> getMembersByActivityStatus(@PathVariable boolean status) {
        List<Member> members = memberService.findMembersByActivityStatus(status);
        return ResponseEntity.ok().body(members);
    }

    @GetMapping("/after-date")
    public ResponseEntity<List<Member>> getMembersByAfterDate(
            @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Member> members = memberService.findMembersJoinedAfter(date);
        return ResponseEntity.ok().body(members);
    }

    @PutMapping("/update-membership")
    public ResponseEntity<String> updateMembershipTypeIfBlank(@RequestBody String newType) {
        memberService.updateMembershipTypeIfBlank(newType);
        return ResponseEntity.ok().body("The new type has been updated: " + newType);
    }

    @DeleteMapping("/delete-joined-before")
    public ResponseEntity<String> deleteMemberJoinedBefore(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        memberService.deleteMemberBeforeDate(date);
        return ResponseEntity.ok().body("The member has been deleted. Date: " + date);
    }

    @PostMapping("/add-new")
    public ResponseEntity<Member> addNewMember(@RequestBody Member member) {
        Member memberAdded = memberService.addNewMember(member);
        return ResponseEntity.ok().body(memberAdded);
    }
}
