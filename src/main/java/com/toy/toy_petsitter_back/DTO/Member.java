package com.toy.toy_petsitter_back.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Member {

    public Member() {}

    private String userKey;
    private String email;
    private String authority;
}
