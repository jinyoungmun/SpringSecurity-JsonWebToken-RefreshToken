package com.webmister.semicolon.repository;

import com.webmister.semicolon.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
