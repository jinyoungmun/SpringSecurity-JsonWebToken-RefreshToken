package com.webmister.semicolon.repository;

import com.webmister.semicolon.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findByAuthorityName(String authorityName);

}
