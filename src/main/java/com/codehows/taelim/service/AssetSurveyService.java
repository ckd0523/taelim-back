package com.codehows.taelim.service;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.dto.DeleteRequest;
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
import org.springframework.transaction.annotation.Transactional;

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
        Member member = memberRepository.findByEmail(email);
                //.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

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
        //검증완료하여 등록 완료
        List<CommonAsset> commonAssets = commonAssetRepository.findAssetNoByAssetLocation(assetLocation);

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
    //원래는 엔티티 자체를 받아서 넘겨주려 했지만
    //자산 조사 이력을 조회할 때 자산 조사를 등록한 사람의 이름을 가져오기 위해 Dto를 사용
    //member의 이름이 아니라 email로 fk가 잡혀있기 때문에 쿼리 dsl로
    //내가 원하는 컬럼의 정보를 가져와야하기 때문
    //그래서 쿼리 dsl에서 Projections.constructor를 사용하여
    //entity에 매핑시켜주지 않고 dto에 매핑
    public List<AssetSurveyHistoryDto> getAssetSurveyHistory() {
        return assetSurveyHistoryRepository.findAssetSurveyHistoryAndMemberName();
    }

    //자산 조사 삭제
    //자산 조사를 삭제하려면 조사 중인 조사만 삭제 가능
    //자산 조사 삭제는 등록한 사람만 가능하다는 조건이나 동작 필요, 시큐리티에서 하는건가?
    //삭제할 때 transactional 어노테이션 안 붙이면 오류남
    //이유는 deleteByAssetSurveyNoIn이 메서드가 네이티브 쿼리로 작성되었기 때문
    //Jpa에서 제공하는 기본 crud는 붙일 필요 없음
    @Transactional
    public String deleteAssetSurveyHistory(List<Long> assetSurveyNo) {
        //조사 상태가 미완료(false)인 것만 삭제 가능
        //프론트에서 처리 완료
        /*
        if(surveyStatus){
            System.out.println("자산 조사 상태가 미완료인 것만 삭제 가능");
            return "완료된 자산 조사는 삭제 불가";
        }
        */
        //자산 조사 번호가 fk로 연결되어있기 때문에 자산 조사 상세를 먼저 삭제해야함
        assetSurveyDetailRepository.deleteByAssetSurveyNoIn(assetSurveyNo);

        //그 다음 자산 조사 삭제
        assetSurveyHistoryRepository.deleteByAssetSurveyNoIn(assetSurveyNo);

        return "자산 조사 삭제 성공";
    }

    //자산 조사를 위한 자산 조사 상세 이력
    public List<AssetSurveyDetail> getAssetSurveyDetail(Long assetSurveyNo) {
        //assetSurveyDetailRepository에서 findAllByAssetSurveyNo 메서드가
        //받을 매개변수의 예상이 assetSurveyHistory라고 해서
        //프론트에서 넘어온 assetSurveyNo로 assetSurveyHistory를 찾아서
        //고대로 넣어줌, 연관 매핑이 되어있어서 가능한듯
        AssetSurveyHistory assetSurveyHistory = assetSurveyHistoryRepository.findByAssetSurveyNo(assetSurveyNo);
        return assetSurveyDetailRepository.findAllByAssetSurveyNo(assetSurveyHistory);
    }

    //자산 조사 상세 수정
    //자산 조사를 진행할 때 각 자산에 대한 정위치 유무, 파손 유무 등을 실시간으로 처리
    //public String updateAssetSurveyDetail() {}
}