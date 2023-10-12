package com.limvik.backend.repository;

import com.limvik.backend.domain.Application;
import com.limvik.backend.domain.ApplicationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationKey> {

}
