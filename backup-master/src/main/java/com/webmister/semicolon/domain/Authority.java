package com.webmister.semicolon.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "authority")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name ="authorityName")
    public String authorityName;
}
