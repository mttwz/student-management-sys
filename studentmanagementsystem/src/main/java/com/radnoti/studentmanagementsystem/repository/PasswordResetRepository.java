package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Passwordreset;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetRepository  extends CrudRepository<Passwordreset, Integer> {
}
