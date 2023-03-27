package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Passwordreset;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository  extends CrudRepository<Passwordreset, Integer> {

    @Query("select pwr from Passwordreset pwr " +
            "where pwr.userId.id = :userId " +
            "and pwr.isUsed = false " +
            "order by pwr.expireDate desc")
    List<Passwordreset> getLastValidResetCode(Integer userId, Pageable pageable);

}
