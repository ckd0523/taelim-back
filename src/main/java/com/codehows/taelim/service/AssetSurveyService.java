package com.codehows.taelim.service;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyDetailDto;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.dto.AssetSurveyHistoryRegisterDto;
import com.codehows.taelim.dto.AssetSurveyUpdateDto;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetSurveyService {

    private final AssetSurveyHistoryRepository assetSurveyHistoryRepository;
    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final AssetSurveyDetailRepository assetSurveyDetailRepository;

    //자산 조사 등록
    public Boolean assetSurveyRegister(AssetSurveyHistoryRegisterDto registerData) {
        System.out.println("sjdsklfjklsdfjl");
        //프론트에서 받아온 2개 값을 추출
        String email = registerData.getEmail();

        System.out.println("여기 뭔데 : " + registerData.getLocation());
        AssetLocation location = AssetLocation.valueOf(registerData.getLocation());
        Long round = registerData.getRound();
        //long round = 1;

        //등록할 때 프론트에서 유저의 email을 가져와서 DB에 조회 후 Member를 가져와서 자산 조사자에 넣어준다.
        //optional을 사용하여 orElseThrow로 예외 처리 가능, 원래는
        //Optional<Member> member = memberRepository.findByEmail("test@example.com");
        //Member foundMember = member.get();
        //이렇게 해줘야하는데 orElseThrow를 쓰면 값이 있으면 optional을 Member로 자동 변환해줌.
        //없으면 예외 처리
        Member member = memberRepository.findByEmail(email);
        //.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //이 부분은 자산 조사 등록 과정에서 이미 판별 완료
        /*//해당 위치에 대해 중복 자산 조사 등록이 안되도록 하기 위해 해당 위치의 마지막 자산 조사 이력 가져옴
        AssetSurveyHistory lastAssetSurveyHistory = assetSurveyHistoryRepository.findByAssetSurveyLocation(location);

        //해당 위치에 대해 한번도 조사가 이루어지지 않았을 경우 바로 등록
        if(lastAssetSurveyHistory == null) {
            createAssetSurveyHistory(member, round, location); //round = 1
        } else {
            //해당 위치에 대한 조사가 진행 중인게 있다면 등록 불가
            if(lastAssetSurveyHistory.getSurveyStatus()) {
                return false;
            }
            //조사하려는 위치로 해당 위치의 회차가 몇인지 조회하여 +1 회차로 계산
            round = lastAssetSurveyHistory.getRound() + 1;
            createAssetSurveyHistory(member, round, location);
        }*/

        try {
            createAssetSurveyHistory(member, round, location);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void createAssetSurveyHistory(Member member, long round, AssetLocation location) {
        AssetSurveyHistory assetSurveyHistory = AssetSurveyHistory.builder()
                .member(member)
                .round(round)
                .assetSurveyLocation(location)
                .assetSurveyStartDate(LocalDate.now())
                .build();

        //자산 조사 등록
        AssetSurveyHistory savedAssetSurveyHistory = assetSurveyHistoryRepository.save(assetSurveyHistory);
        //자산 조사 상세 등록
        //자산 조사 등록에서 반환된 entity를 그대로 다시 넣어줌, 연관매핑이 되어있어서 가능한듯
        assetSurveyDetailRegister(location ,savedAssetSurveyHistory);
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
    //2024-09-11 발견된 문제점, dto에 매핑해줄 때 db 컬럼에 null이 있으면 매핑을 못해줌
    //그래서 하나라도 null이 들어가면 그 레코드는 List에 들어가지 않음
    //하지만 이는 프론트와 백엔드 코드를 잘 짜 놓으면 문제 없음
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
    public boolean deleteAssetSurveyHistory(List<Long> assetSurveyNo) {
        try {
            //자산 조사 번호가 fk로 연결되어있기 때문에 자산 조사 상세를 먼저 삭제해야함
            assetSurveyDetailRepository.deleteByAssetSurveyNoIn(assetSurveyNo);

            //그 다음 자산 조사 삭제
            assetSurveyHistoryRepository.deleteByAssetSurveyNoIn(assetSurveyNo);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    //자산 조사를 위한 자산 조사 상세 이력
    public List<AssetSurveyDetailDto> getAssetSurveyDetail(Long assetSurveyNo) {
        List<AssetSurveyDetailDto> assetSurveyDetailDtoList = new ArrayList<>();

        //assetSurveyDetailRepository에서 findAllByAssetSurveyNo 메서드가
        //받을 매개변수의 예상이 assetSurveyHistory라고 해서
        //프론트에서 넘어온 assetSurveyNo로 assetSurveyHistory를 찾아서
        //고대로 넣어줌, 연관 매핑이 되어있어서 가능한듯
        AssetSurveyHistory assetSurveyHistory = assetSurveyHistoryRepository.findByAssetSurveyNo(assetSurveyNo);

        //일단 자산 조사 상세를 전부 가져옴
        List<AssetSurveyDetail> assetSurveyDetailList = assetSurveyDetailRepository.findAllByAssetSurveyNo(assetSurveyHistory);

        //findLatestData는 용대형이 merge를 해줘야함
        //해당 위치에 대한 최신 자산 정보를 가져옴
        List<CommonAsset> commonAssets = commonAssetRepository.findDetailByLocation(assetSurveyHistory.getAssetSurveyLocation());

        for(AssetSurveyDetail assetSurveyDetail : assetSurveyDetailList) {
            //자산 조사 상세의 자산 번호로 공통 정보에 있는 자산 코드, 자산명 등을 가져와야함
            //이렇게 참조를 해서 가져오면
            //완료된 자산 조사를 들어가서 자산 조사 상세를 보게 되면
            //자산 조사 상세의 각 자산의 정보가 자산 조사를 했던 당시의 데이터가 아니게 됨.
            //하지만 자산 상세에서 변경 이력을 볼 수 있으므로 추적을 가능하겠지만
            //매우 불편한 형식이 될것으로 예상

            //자산 조사 상세 정보와 최신 자산 정보를 dto로 다시 만듦
            for(CommonAsset commonAsset : commonAssets){
                AssetSurveyDetailDto assetSurveyDetailDto = new AssetSurveyDetailDto(commonAsset, assetSurveyDetail);
                assetSurveyDetailDtoList.add(assetSurveyDetailDto);
            }
        }
        return assetSurveyDetailDtoList;
    }
    */

    //자산 조사를 위한 자산 조사 상세 이력
    public List<AssetSurveyDetailDto> getAssetSurveyDetail(Long assetSurveyNo) {
        // 자산 조사 이력 조회
        AssetSurveyHistory assetSurveyHistory = assetSurveyHistoryRepository.findByAssetSurveyNo(assetSurveyNo);

        // 자산 조사 상세 리스트 조회
        List<AssetSurveyDetail> assetSurveyDetailList = assetSurveyDetailRepository.findAllByAssetSurveyNo(assetSurveyHistory);

        // 공통 자산 정보를 자산 번호별로 매핑
        List<Long> assetNos = assetSurveyDetailList.stream()
                .map(assetSurveyDetail -> assetSurveyDetail.getAssetNo().getAssetNo()) // CommonAsset의 assetNo 가져오기
                .collect(Collectors.toList());

        List<CommonAsset> commonAssets = commonAssetRepository.findAllById(assetNos);
        Map<Long, CommonAsset> commonAssetMap = commonAssets.stream()
                .collect(Collectors.toMap(CommonAsset::getAssetNo, commonAsset -> commonAsset));

        // DTO로 변환
        return assetSurveyDetailList.stream()
                .map(assetSurveyDetail -> {
                    // 자산 조사 상세의 assetNo로 공통 자산 정보 매핑
                    CommonAsset commonAsset = commonAssetMap.get(assetSurveyDetail.getAssetNo().getAssetNo());

                    // DTO로 변환
                    return new AssetSurveyDetailDto(commonAsset, assetSurveyDetail);
                })
                .collect(Collectors.toList());
    }

    //자산 조사 등록 시 해당 위치에 대해 진행 중인 조사가 있는지 확인
    public boolean checkLocation(AssetLocation location) {
        //AssetLocation assetLocation = AssetLocation.valueOf(location);
        Optional<AssetSurveyHistory> isExist = assetSurveyHistoryRepository.findLastAssetSurvey(location, false);

        if(isExist.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //자산 조사 등록 시 화면에 줄 해당 위치에 대한 회차 정보
    public Long getNextRoundForLocation(AssetLocation location) {
        Optional<AssetSurveyHistory> isExistRound = assetSurveyHistoryRepository.findLastAssetSurvey(location, true);

        //if문을 간단하게 해결할 수 있다.
        /*if(isExistRound.isEmpty()) {
            return 1L;
        } else {
            return isExistRound.get().getRound() + 1L;
        }*/
        return isExistRound.map(assetSurveyHistory -> assetSurveyHistory.getRound() + 1).orElse(1L);
    }

    //자산 조사 완료
    public boolean completeSurvey(Long assetSurveyNo) {
        try {
            LocalDate now = LocalDate.now();
            assetSurveyHistoryRepository.completeSurvey(assetSurveyNo, now);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //자산 조사 상세 수정
    //자산 조사를 진행할 때 각 자산에 대한 정위치 유무, 파손 유무 등을 실시간으로 처리
    @Transactional
    public boolean updateAssetSurveyDetail(AssetSurveyUpdateDto updateDto) {
        try {
            assetSurveyDetailRepository.updateAssetSurveyDetail(updateDto);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

