package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.Authority;
import lombok.Data;

@Data
public class AuthorityDto {
    public String authorityName;

    public AuthorityDto(Authority authority){
        this.authorityName = authority.getAuthorityName();
    }
}
