package com.codehows.taelim.APIController;


import com.codehows.taelim.dto.MaintainDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintain")
public class MaintainController {

    @PostMapping("/register")
    public ResponseEntity<Long> maintainRegister(@RequestBody MaintainDto maintainDto) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
