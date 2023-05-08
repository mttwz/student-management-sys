package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupmembers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkgroupMembersRepository extends CrudRepository<Workgroupmembers, Integer> {

    @Query("select wm.userId.id from Workgroupmembers wm " +
            "where wm.workgroupId.id = :workgroupId")
    List<Integer> getAllUserIdFromWorkgroup(Integer workgroupId);

    @Modifying
    @Query("delete from Workgroupmembers wm " +
            "where wm.workgroupId.id = :workgroupId " +
            "and wm.userId.id = :userId")
    void removeUserFromWorkgroup(Integer workgroupId, Integer userId);
}
