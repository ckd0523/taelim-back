package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.UserDto;
import com.codehows.taelim.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/user/search")
    public List<UserDto> searchUsersByName(@RequestParam("name") String name) {


        // 서비스에서 변환된 UserDto 리스트를 받아서 바로 반환
        return userService.searchUsersByFullname(name);
    }
}
