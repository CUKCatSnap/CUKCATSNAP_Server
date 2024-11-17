package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.document.Holiday;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, String> {


}
