package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/workgroup")
@CrossOrigin(origins = "${cross-origin}")
@RequiredArgsConstructor
public class WorkgroupController {


    private final WorkgroupService workgroupService;

    private final UserService userService;


    /**
     * Creates a workgroup.
     *
     * @param workgroupDto the WorkgroupDto object containing the workgroup details.
     * @return a ResponseDto object containing the response.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto createWorkgroup(@RequestBody WorkgroupDto workgroupDto) {
        return workgroupService.createWorkgroup(workgroupDto);

    }

    /**
     * Deletes a workgroup.
     *
     * @param workgroupId the ID of the workgroup to be deleted.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-workgroup/{workgroupId}")
    public void deleteWorkgroup(@PathVariable String workgroupId) {
        workgroupService.deleteWorkgroup(workgroupId);
    }


    /**
     * Restores a deleted workgroup.
     *
     * @param workgroupId the ID of the workgroup to be restored.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/restore-deleted-workgroup/{workgroupId}")
    public void restoreDeleteWorkgroup(@PathVariable String workgroupId) {
        workgroupService.restoreDeletedWorkgroup(workgroupId);
    }


    /**
     * Adds a user to a workgroup.
     *
     * @param workgroupmembersDto the WorkgroupmembersDto object containing the user and workgroup details.
     * @return a ResponseDto object containing the response.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return workgroupService.addUserToWorkgroup(workgroupmembersDto);
    }

    /**
     * Removes a user from a workgroup.
     *
     * @param workgroupmembersDto the WorkgroupmembersDto object containing the user and workgroup details.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/remove-user-from-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public void removeUserFromWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        workgroupService.removeUserFromWorkgroup(workgroupmembersDto);
    }

    /**
     * Retrieves all workgroups.
     *
     * @param pageable the pagination information.
     * @return a PagingDto object containing the workgroup results.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-all-workgroups")
    public PagingDto getAllWorkgroups(Pageable pageable) {
        return workgroupService.getAllWorkgroup(pageable);
    }


    /**
     * Retrieves the information of a workgroup.
     *
     * @param workgroupId the ID of the workgroup.
     * @return a ResponseEntity containing the WorkgroupInfoDto object representing the workgroup.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-workgroup-info/{workgroupId}")
    public ResponseEntity<WorkgroupInfoDto> getWorkgroupInfo(@PathVariable String workgroupId){
        return ResponseEntity.ok(workgroupService.getWorkgroupInfo(workgroupId));
    }


    /**
     * Edits the information of a workgroup.
     *
     * @param workgroupId      the ID of the workgroup.
     * @param workgroupInfoDto the WorkgroupInfoDto object containing the updated workgroup information.
     * @return a ResponseDto object containing the response.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-workgroup-info/{workgroupId}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto editWorkgroupInfo(@PathVariable String workgroupId, @RequestBody WorkgroupInfoDto workgroupInfoDto) {
        return workgroupService.editWorkgroupInfo(workgroupId,workgroupInfoDto);
    }



    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @GetMapping(path = "/search-addable-users/{workgroupId}{q}")
    public PagingDto searchAddableUsers(@RequestParam(required = false) String q, @PathVariable String workgroupId, Pageable pageable){
        return workgroupService.searchAddableUsers(q,workgroupId,pageable);

    }


}
