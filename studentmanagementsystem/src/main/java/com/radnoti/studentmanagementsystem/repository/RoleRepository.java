package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}