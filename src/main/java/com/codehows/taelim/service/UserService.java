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


//    // 이름으로 검색 - 현재 디코딩일때 데이터
//    public List<UserDto> searchUsersByFullname(String fullname) {
//        // 입력값을 BASE64로 인코딩
//        System.out.println("검색어 : " + fullname);
//        String encodedFullname = encodeBase64(fullname);
//        System.out.println("인코딩 : " + encodedFullname);
//
//        List<AspNetUser> users = aspNetUserRepository.findByEncodedFullname(encodedFullname);
//
//        System.out.println("반환값 : "+users);
//
//        return users.stream()
//                .map(user -> new UserDto(
//                        user.getId(),// GUID를 Long 타입으로 변환
//                        new String(Base64.getDecoder().decode(user.getUsername())), // 계정명 디코딩
//                        new String(Base64.getDecoder().decode(user.getFullname())), // 실명 디코딩
//                        new String(Base64.getDecoder().decode(user.getDepartment())) // 부서명 디코딩
//                ))
//                .collect(Collectors.toList());
//    }
//    // 자산조회에서 id 를 불러오는 형태
//    public UserDto getUserById(String id) {
//        if (id == null || id.isEmpty()) {
//            return null;
//        } else {
//            AspNetUser user = aspNetUserRepository.findById(id).orElseThrow();
//            UserDto userDto = new UserDto();
//            userDto.setId(user.getId());
//            userDto.setUsername(decodeBase64(user.getUsername())); // 변경된 부분
//            userDto.setFullname(decodeBase64(user.getFullname())); // 변경된 부분
//            userDto.setDepartment(decodeBase64(user.getDepartment())); // 변경된 부분
//            return userDto;
//        }
//    }
    //
     //이름으로 검색 - 태림측 사용방식
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
                        new String(decodeBase64(user.getDepartment()))// 부서명 디코딩
                ))
                .collect(Collectors.toList());
    }
    // 자산조회에서 id 를 불러오는 형태  - 태림측 사용방식
    public UserDto getUserById(String id) {
        System.out.println("유저서비스1: " + id);
        if(id == null || id.isEmpty()) {
            return null;
        } else {
            AspNetUser user = aspNetUserRepository.findById(id).orElseThrow();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setFullname(user.getFullname());
            userDto.setDepartment(user.getDepartment());
            return userDto;
        }
    }




    public List<String> getUserIdsByFullname(String fullname) {
        String encodedFullname = encodeBase64(fullname); // 변경된 부분

        List<AspNetUser> users = aspNetUserRepository.findByFullnameContaining(encodedFullname);

        if (users.isEmpty()) {
            System.out.println("No users found with the given fullname: " + fullname);
            return Collections.emptyList();
        }

        return users.stream()
                .map(AspNetUser::getId)
                .collect(Collectors.toList());
    }
}
