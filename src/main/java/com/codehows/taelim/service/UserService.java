package com.codehows.taelim.service;

import com.codehows.taelim.dto.UserDto;
import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AspNetUserRepository aspNetUserRepository;
    private final Base64SetService base64SetService;

    //인코딩
//    public String encodeBase64(String plainText) {
//        return Base64.getEncoder().encodeToString(plainText.getBytes());
//    }
    //인코딩 제외
    // public String encodeBase64(String plainText) {
    //     return plainText;
    // }

    //디코딩
//    public String decodeBase64(String encodedString) {
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//        return new String(decodedBytes);
//    }
    //디코딩 제외
    // public String decodeBase64(String encodedString) {
    //     return encodedString;
    // }

    //인코딩
    public String encodeBase64(String plainText) {
        if(base64SetService.getBase64Config().isEnabled()){
            return Base64.getEncoder().encodeToString(plainText.getBytes());
        }else{
            return plainText;
        }
    }

    //디코딩
    public String decodeBase64(String encodedString) {
        if(base64SetService.getBase64Config().isEnabled()){
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            return new String(decodedBytes);
        }else{
            return encodedString;
        }
    }


    // 이름으로 검색
    public List<UserDto> searchUsersByFullname(String fullname) {
        // 입력값을 BASE64로 인코딩
        System.out.println("검색어 : " + fullname);
        String encodedFullname = encodeBase64(fullname);
        System.out.println("인코딩 : " + encodedFullname);

        List<AspNetUser> users = aspNetUserRepository.findByEncodedFullname(encodedFullname);

        System.out.println("반환값 : "+users);

        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),// GUID를 Long 타입으로 변환
                        new String(decodeBase64(user.getUsername())), // 계정명 디코딩
                        new String(decodeBase64(user.getFullname())), // 실명 디코딩
                        user.getDepartment() // 부서명 디코딩
                ))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        System.out.println("유저서비스1: " + id);
        if(id == null || id.isEmpty()) {
            return null;
        } else {
            AspNetUser user = aspNetUserRepository.findById(id).orElseThrow();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(decodeBase64(user.getUsername()));
            userDto.setFullname(decodeBase64(user.getFullname()));
            userDto.setDepartment(user.getDepartment());
            return userDto;
        }
    }



    // 자산 조회에서 사용자를 위한 검색 조건에 필요한거
    public List<String> getUserIdsByFullname(String fullname) {
        // fullname을 Base64로 인코딩
        String encodedFullname = Base64.getEncoder().encodeToString(fullname.getBytes(StandardCharsets.UTF_8));

        List<AspNetUser> users = aspNetUserRepository.findByFullnameContaining(encodedFullname);

        if (users.isEmpty()) {
            System.out.println("No users found with the given fullname: " + fullname);
            return Collections.emptyList(); // 빈 리스트 반환
        }

        return users.stream()
                .map(AspNetUser::getId)
                .collect(Collectors.toList());
    }
}
