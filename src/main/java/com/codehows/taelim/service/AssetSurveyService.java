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
    private final CommonAssetRepository commonAssetRepository;
    private final AssetSurveyDetailRepository assetSurveyDetailRepository;
    private final UserService userService;

    //자산 조사 등록
    public Boolean assetSurveyRegister(AssetSurveyHistoryRegisterDto registerData) {

        AssetLocation location = AssetLocation.valueOf(registerData.getLocation());
        Long round = registerData.getRound();
        String assetSurveyBy = registerData.getUserId();

        try {
            createAssetSurveyHistory(assetSurveyBy, round, location);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void createAssetSurveyHistory(String assetSurveyBy, long round, AssetLocation location) {
        AssetSurveyHistory assetSurveyHistory = AssetSurveyHistory.builder()
                .assetSurveyBy(assetSurveyBy)
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
        List<AssetSurveyHistoryDto> list = assetSurveyHistoryRepository.findAssetSurveyHistoryAndMemberName();

        for(AssetSurveyHistoryDto assetSurveyHistoryDto : list){
            if((assetSurveyHistoryDto.getAssetSurveyBy() == null)){
                assetSurveyHistoryDto.setAssetSurveyBy("Unknown User");
            } else {
                assetSurveyHistoryDto.setAssetSurveyBy(userService.getUserById(assetSurveyHistoryDto.getAssetSurveyBy()).getFullname());
            }

        }

        return list;
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
            if(updateDto.getContent() == null) {
                assetSurveyDetailRepository.updateAssetSurveyDetail(updateDto);
            } else {
                assetSurveyDetailRepository.updateAssetSurveyDetail2(updateDto);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

