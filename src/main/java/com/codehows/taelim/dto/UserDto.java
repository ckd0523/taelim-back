package com.codehows.taelim.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username; //계정명
    private String fullname; //실제 사용자 이름
    private String department; //사용자의 부서
}
