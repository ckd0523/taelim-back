package com.codehows.taelim.controller;

import com.codehows.taelim.dto.EmailSetDto;
import com.codehows.taelim.entity.EmailSet;
import com.codehows.taelim.service.EmailSetService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailSets")
public class EmailSetController {
    private final EmailSetService emailSetService;

    // 모든 EmailSet 조회
    @GetMapping("/AllEmailSets")
    public List<EmailSet> getAllEmailSets() {
        return emailSetService.getAllEmailSet();
    }

    // EmailSet 추가
    @PostMapping("/AddEmailSet")
    public EmailSet addEmailSet(@RequestBody EmailSetDto emailSetDto) {
        EmailSet emailSet = new EmailSet();
        emailSet.setSetName(emailSetDto.getSetName());
        emailSet.setSetEmail(emailSetDto.getSetEmail());
        emailSet.setIsSelected(emailSetDto.getIsSelected());

        return emailSetService.addEmailSet(emailSet);
    }

    // EmailSet 수정
    @PutMapping("/UpdateEmailSet")
    public ResponseEntity<EmailSet> updateEmailSet(@RequestBody EmailSetDto emailSetDto) {
        try {
            EmailSet updatedEmailSet = emailSetService.updateEmailSet(
                    emailSetDto.getEmailSetNo(),
                    emailSetDto.getSetEmail(),
                    emailSetDto.getSetName()
            );
            return ResponseEntity.ok(updatedEmailSet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 선택된 EmailSet 변경
    @PutMapping("/{emailSetNo}/select")
    public void selectEmailSet(@PathVariable Long emailSetNo) {
        emailSetService.selectEmailSet(emailSetNo);
    }
}
