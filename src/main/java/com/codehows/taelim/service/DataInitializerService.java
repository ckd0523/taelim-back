package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class DataInitializerService {

    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;

    @Transactional
    public void insertDummyData() {
        Member testMember = new Member();
        testMember.setEmail("testadmin@naver.com");
        testMember.setPassword("1234");
        testMember.setRole(Role.ADMIN);
        testMember.setUName("테스트관리자");

        memberRepository.save(testMember);

        // CommonAsset 데이터 삽입
        for (int i = 1; i <= 10000; i++) {
            Random random = new Random();
            int randomInt = random.nextInt(100);
            int randomInt2 = random.nextInt(3) + 1;

            boolean randomBoolean = random.nextBoolean();

            CommonAsset asset = new CommonAsset();
            asset.setAssetClassification(AssetClassification.FURNITURE);
            asset.setAssetBasis(AssetBasis.COMMON);
            asset.setAssetCode(String.format("ASSET%03d", randomInt));
            asset.setAssetName("가구 " + randomInt);
            asset.setPurpose("Test Purpose");
            asset.setDepartment(Department.ADMINISTRATIVE_DEPARTMENT);
            asset.setAssetLocation(AssetLocation.MAIN_1F);
            asset.setAssetUser(testMember);

            //승인 여부
            if(randomInt2 == 1) {
                asset.setApproval(Approval.APPROVE);
                //요청 여부
                asset.setRequestStatus(false);
                //폐기 여부
                //asset.setDisposalStatus(randomBoolean);
                asset.setDisposalStatus(false);
            } else if(randomInt2 == 2) {
                asset.setApproval(Approval.REFUSAL);
                //요청 여부
                asset.setRequestStatus(true);
                //폐기 여부
                //asset.setDisposalStatus(randomBoolean);
                asset.setDisposalStatus(false);
            } else {
                asset.setApproval(Approval.UNCONFIRMED);
                //요청 여부
                asset.setRequestStatus(randomBoolean);
                //폐기 여부
                //asset.setDisposalStatus(randomBoolean);
                asset.setDisposalStatus(false);
            }

            // 기타 필드 설정
            commonAssetRepository.save(asset);
        }
    }
}
