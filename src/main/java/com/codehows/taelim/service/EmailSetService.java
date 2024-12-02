package com.codehows.taelim.service;

import com.codehows.taelim.entity.EmailSet;
import com.codehows.taelim.repository.EmailSetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailSetService {
    private final EmailSetRepository emailSetRepository;

    public List<EmailSet> getAllEmailSet() {return emailSetRepository.findAll();}

    public EmailSet addEmailSet(EmailSet emailSet) {
        long count = emailSetRepository.count();
        if(count >=2 ){
            throw new IllegalArgumentException("이메일 수신인은 2명까지만 등록할수 있습니다.");
        }
        if(count == 1){
            emailSet.setIsSelected(false);
        } else {
            emailSet.setIsSelected(true);
        }
        return emailSetRepository.save(emailSet);
    }

    public EmailSet updateEmailSet(Long id, String email, String name){
        EmailSet emailSet = emailSetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("이메일수신인을 찾을수 없습니다."));
        emailSet.setSetEmail(email);
        emailSet.setSetName(name);
        return emailSetRepository.save(emailSet);
    }

    public void selectEmailSet(Long id){
        List<EmailSet> emailSetList = emailSetRepository.findAll();
        for(EmailSet emailSet : emailSetList){
            emailSet.setIsSelected(false);
        }

        EmailSet selectedEmailSet = emailSetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("이메일수신인을 찾을수 없습니다."));
        selectedEmailSet.setIsSelected(true);
        emailSetRepository.saveAll(emailSetList);
        emailSetRepository.save(selectedEmailSet);
    }

    public EmailSet getSelectedEmailSet(){
        Optional<EmailSet> selectedEmailSet = emailSetRepository.findByIsSelectedTrue();
        return selectedEmailSet.orElse(null);
    }
}
