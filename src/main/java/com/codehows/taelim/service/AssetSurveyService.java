package com.codehows.taelim.service;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.AssetSurveyDetail;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.repository.AssetSurveyDetailRepository;
import com.codehows.taelim.repository.AssetSurveyHistoryRepository;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetSurveyService {

    private final AssetSurveyHistoryRepository assetSurveyHistoryRepository;
    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final AssetSurveyDetailRepository assetSurveyDetailRepository;

    //자산 조사 등록
    public String assetSurveyRegister(AssetLocation assetLocation, Long round, String email) {
        //등록할 때 프론트에서 유저의 email을 가져와서 DB에 조회 후 Member를 가져와서 자산 조사자에 넣어준다.
        //optional을 사용하여 orElseThrow로 예외 처리 가능, 원래는
        //Optional<Member> member = memberRepository.findByEmail("test@example.com");
        //Member foundMember = member.get();
        //이렇게 해줘야하는데 orElseThrow를 쓰면 값이 있으면 optional을 Member로 자동 변환해줌.
        //없으면 예외 처리
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        AssetSurveyHistory assetSurveyHistory = new AssetSurveyHistory();
        assetSurveyHistory.setAssetSurveyLocation(assetLocation); //자산 위치
        assetSurveyHistory.setRound(round); //자산 조사 회차, 회차는 프론트에서 계산해서 가져오거나 백에서 다시 sql 날려서 찾아서 넣을 수 있음
        assetSurveyHistory.setAssetSurveyBy(member); //자산 조사자
        assetSurveyHistory.setAssetSurveyStartDate(LocalDate.now()); //자산 조사 시작일
        assetSurveyHistory.setSurveyStatus(false); //자산 조사 상태, false = 미완료, true = 완료

        //자산 조사 등록
        AssetSurveyHistory savedAssetSurveyHistory = assetSurveyHistoryRepository.save(assetSurveyHistory);

        //자산 조사 상세 등록
        assetSurveyDetailRegister(assetLocation ,savedAssetSurveyHistory);

        return "자산 등록 성공";
    }

    //자산 조사 상세 등록(자산 조사 등록 후 조사할 자산들을 등록)
    public void assetSurveyDetailRegister(AssetLocation assetLocation ,AssetSurveyHistory assetSurveyNo) {
        //자산 번호(최신 자산)를 가져와서 넣어서 save 해야함
        //그런데 공통 자산 테이블에는 최신 정보만 있는 것이 아니라서 쿼리DSL 써서
        //최신 정보만 가져오는 select문 검증 필요, 더미 데이터가 좀 잇어야할 듯
        List<CommonAsset> commonAssets = commonAssetRepository.findByAssetLocation(assetLocation);

        for(CommonAsset commonAsset : commonAssets){
            AssetSurveyDetail assetSurveyDetail = new AssetSurveyDetail();
            assetSurveyDetail.setAssetNo(commonAsset); //자산 번호
            assetSurveyDetail.setAssetSurveyNo(assetSurveyNo); //자산 조사 번호
            assetSurveyDetail.setAssetStatus(false); //자산 상태, false = 비정상, true = 정상
            assetSurveyDetail.setExactLocation(false); //자산 정위치 유무, false = 무, true = 유
            assetSurveyDetailRepository.save(assetSurveyDetail);
        }
    }

    //자산 조사 이력 조회
    public List<AssetSurveyHistory> getAssetSurveyHistory() {
        return assetSurveyHistoryRepository.findAll();
    }

    //자산 조사 삭제
    //자산 조사를 삭제하려면 조사 중인 조사만 삭제 가능
    //자산 조사 삭제는 등록한 사람만 가능하다는 조건이나 동작 필요, 시큐리티에서 하는건가?
    public String deleteAssetSurveyHistory(Long assetSurveyHistoryNo, Boolean surveyStatus) {
        //조사 상태가 미완료(false)인 것만 삭제 가능
        if(surveyStatus){
            System.out.println("자산 조사 상태가 미완료인 것만 삭제 가능");
            return "완료된 자산 조사는 삭제 불가";
        }
        else {
            //자산 조사 상세를 먼저 삭제해야함
            assetSurveyDetailRepository.deleteById(assetSurveyHistoryNo);

            //자산 조사 삭제
            assetSurveyHistoryRepository.deleteById(assetSurveyHistoryNo);

            return "자산 조사 삭제 성공";
        }
    }
}