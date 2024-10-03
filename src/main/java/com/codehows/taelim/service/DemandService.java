package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.DemandHistoryAllDto;
import com.codehows.taelim.dto.DemandHistoryDto;
import com.codehows.taelim.dto.UnconfirmedDemandDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.entity.DemandDtl;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.DemandDtlRepository;
import com.codehows.taelim.repository.DemandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DemandService {

    private final DemandDtlRepository demandDtlRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final DemandRepository demandRepository;

    public List<DemandHistoryDto> getAllDemandHistory() {

        List<DemandDtl> demandDtls = demandDtlRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()){
                Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                Demand demand1 = demand.orElseThrow();
                if(demand1.getDisposeLocation() == null){
                    //수정이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("update");
                }else {
                    //폐기이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("delete");
                }
                demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                demandHistoryDto.setAssetNo(asset.getAssetNo());
                demandHistoryDto.setAssetCode(asset.getAssetCode());
                demandHistoryDto.setDemandDate(asset.getCreateDate());
                demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                demandHistoryDtos.add(demandHistoryDto);
            }

        }

        return demandHistoryDtos;

    }

    public List<DemandHistoryDto> getAssetDemandHistory(List<CommonAsset>commonAssets) {

        List<DemandDtl> demandDtls = demandDtlRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()){
                Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                Demand demand1 = demand.orElseThrow();
                if(demand1.getDisposeLocation() == null){
                    //수정이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("update");
                }else {
                    //폐기이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("delete");
                }
                demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                demandHistoryDto.setAssetNo(asset.getAssetNo());
                demandHistoryDto.setAssetCode(asset.getAssetCode());
                demandHistoryDto.setDemandDate(asset.getCreateDate());
                demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                demandHistoryDtos.add(demandHistoryDto);
            }

        }

        return demandHistoryDtos;

    }

    private final AssetService assetService;
    // 미확인 자산 가져오는 서비스
    public List<UnconfirmedDemandDto> getUnconfirmedDemandHistory() {

        List<DemandDtl> demandDtls = demandDtlRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        List<UnconfirmedDemandDto> unconfirmedDemandDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()) {
                if (asset.getApproval() == Approval.UNCONFIRMED) {
                    Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                    Demand demand1 = demand.orElseThrow();
                    if (demand1.getDisposeLocation() == null) {
                        //수정이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("update");
                    } else {
                        //폐기이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("delete");
                    }
                    demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                    demandHistoryDto.setAssetNo(asset.getAssetNo());
                    demandHistoryDto.setAssetCode(asset.getAssetCode());
                    demandHistoryDto.setDemandDate(asset.getCreateDate());
                    demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                    demandHistoryDtos.add(demandHistoryDto);
                }
            }

        }

        for(DemandHistoryDto demandHistoryDto : demandHistoryDtos){
            UnconfirmedDemandDto unconfirmedDemandDto = new UnconfirmedDemandDto();
            if(Objects.equals(demandHistoryDto.getDemandType(), "update")){
                unconfirmedDemandDto.setAssetDto(assetService.getUpdateDetail(demandHistoryDto.getAssetNo()));
                unconfirmedDemandDto.setDemandHistoryDto(demandHistoryDto);
            }else {
                unconfirmedDemandDto.setDemandHistoryDto(demandHistoryDto);
            }
            unconfirmedDemandDtos.add(unconfirmedDemandDto);
        }

        return unconfirmedDemandDtos;

    }

    public List<DemandHistoryDto> getAllDemandHistory1() {

        List<Demand> demands = demandRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();

        for (Demand demand : demands) {

            List<DemandDtl> demandDtls1 = demandDtlRepository.findByDemandNo_DemandNo(demand.getDemandNo());
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();

            // demandDtls1 리스트의 크기 확인
            if (demandDtls1.size() == 1) {
                DemandDtl demandDtl2 = demandDtls1.get(0);
                Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl2.getAssetNo().getAssetNo());
                CommonAsset asset = commonAsset.orElseThrow();

                if (asset.getDemandStatus()) {
                    if (demand.getDisposeLocation() == null) {
                        // 수정이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("update");
                    } else {
                        // 폐기이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("delete");
                    }
                    demandHistoryDto.setDemandNo(demandDtl2.getDemandNo().getDemandNo());
                    demandHistoryDto.setAssetNo(asset.getAssetNo());
                    demandHistoryDto.setAssetCode(asset.getAssetCode());
                    demandHistoryDto.setDemandDate(asset.getCreateDate());
                    demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                    demandHistoryDtos.add(demandHistoryDto);
                }

            } else if (demandDtls1.size() > 1) {
                // 리스트가 여러 개인 경우의 처리
                List<DemandHistoryAllDto> detailAllDtos = new ArrayList<>();
                DemandHistoryDto demandHistoryDto1 = new DemandHistoryDto();
                boolean hasUnconfirmed = false;
                boolean allApprovedOrRefused = true;

                for (DemandDtl demandDtl : demandDtls1) {
                    Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
                    CommonAsset asset = commonAsset.orElseThrow();
                    DemandHistoryAllDto demandHistoryAllDto = new DemandHistoryAllDto();

                    if (asset.getDemandStatus()) {
                        if (demand.getDisposeLocation() == null) {
                            // 수정이력
                            demandHistoryAllDto.setDemandBy("이창현");
                            demandHistoryAllDto.setDemandType("allUpdateDemand");
                        } else {
                            // 폐기이력
                            demandHistoryAllDto.setDemandBy("이창현");
                            demandHistoryAllDto.setDemandType("allDisposeDemand");
                        }
                        demandHistoryAllDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                        demandHistoryAllDto.setAssetNo(asset.getAssetNo());
                        demandHistoryAllDto.setAssetCode(asset.getAssetCode());
                        demandHistoryAllDto.setDemandDate(asset.getCreateDate());
                        demandHistoryAllDto.setDemandStatus(asset.getApproval().toString());
                        detailAllDtos.add(demandHistoryAllDto);

                        // 상태 확인 로직
                        if (asset.getApproval() == Approval.UNCONFIRMED) {
                            hasUnconfirmed = true;
                        } else if (asset.getApproval() != Approval.APPROVE && asset.getApproval() != Approval.REFUSAL) {
                            allApprovedOrRefused = false;
                        }
                    }
                }

                // 상태 결정 로직
                if (hasUnconfirmed) {
                    demandHistoryDto1.setDemandStatus("UNCONFIRMED");
                } else if (allApprovedOrRefused) {
                    demandHistoryDto1.setDemandStatus("completed");
                } else {
                    demandHistoryDto1.setDemandStatus("UNCONFIRMED");
                }
                if (demand.getDisposeLocation() == null) {
                    demandHistoryDto1.setDemandType("allUpdateDemand");
                } else {
                    demandHistoryDto1.setDemandType("allDisposeDemand");
                }
                demandHistoryDto1.setSubRows(detailAllDtos);
                demandHistoryDto1.setDemandDate(demand.getDemandDate());
                demandHistoryDto1.setDemandBy("이창현");
                demandHistoryDtos.add(demandHistoryDto1);
            }
        }

        return demandHistoryDtos;
    }

}
