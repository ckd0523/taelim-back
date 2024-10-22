package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.DemandActionDto;
import com.codehows.taelim.dto.DemandHistoryDto;
import com.codehows.taelim.dto.UnconfirmedDemandDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.entity.DemandDtl;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.DemandDtlRepository;
import com.codehows.taelim.repository.DemandRepository;
import com.codehows.taelim.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class DemandController {

    private final DemandService demandService;
    private final DemandRepository demandRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final DemandDtlRepository demandDtlRepository;

    //요청 리스트 가져오기
    @GetMapping("/DemandHistory")
    public List<DemandHistoryDto> getDemandHistory() {
        return demandService.getAllDemandHistory();
    }

    //요청 리스트 가져오기
    @GetMapping("/DemandList")
    public List<UnconfirmedDemandDto> getUnconfirmedDemandHistory() {
        return demandService.getUnconfirmedDemandHistory();
    }

    @PostMapping("/updateAction")
    public ResponseEntity<String> updateAction(@RequestBody DemandActionDto request) {
        // request 객체에서 데이터를 추출하여 처리
        DemandHistoryDto actionData = request.getDemandAction(); // actionData 배열
        String reason = request.getReason(); // reason 값
        String actionType = request.getActionType(); // actionType 값

        CommonAsset commonAsset = commonAssetRepository.findById(actionData.getAssetNo()).orElse(null);
        DemandDtl demandDtl = demandDtlRepository.findByAssetNo(commonAsset);
        demandDtl.setComment(reason);
        demandDtlRepository.save(demandDtl);
        if (commonAsset != null) {
            if (Objects.equals(actionType, "approve")) {
                commonAsset.setApproval(Approval.APPROVE);
                commonAsset.setDemandCheck(true);
                commonAssetRepository.save(commonAsset);
                demandService.beforeDemand(actionData.getAssetCode(), actionData.getAssetNo());
            } else {
                commonAsset.setApproval(Approval.REFUSAL);
                commonAsset.setDemandCheck(true);
                commonAssetRepository.save(commonAsset);
            }
        }
        // 성공적으로 처리했음을 클라이언트에게 응답
        return ResponseEntity.ok("Update successful");
    }

    @PostMapping("/deleteAction")
    public ResponseEntity<String> deleteAction(@RequestBody DemandActionDto request) {
        DemandHistoryDto actionData = request.getDemandAction(); // actionData 배열
        String reason = request.getReason(); // reason 값
        String actionType = request.getActionType(); // actionType 값

        CommonAsset commonAsset = commonAssetRepository.findById(actionData.getAssetNo()).orElse(null);
        DemandDtl demandDtl = demandDtlRepository.findByAssetNo(commonAsset);
        demandDtl.setComment(reason);
        demandDtlRepository.save(demandDtl);
        if (commonAsset != null) {
            if (Objects.equals(actionType, "approve")) {
                commonAsset.setApproval(Approval.APPROVE);
                commonAsset.setDisposalStatus(true);
                commonAsset.setDemandCheck(true);
                commonAssetRepository.save(commonAsset);
                demandService.beforeDemand(actionData.getAssetCode(), actionData.getAssetNo());
            } else {
                commonAsset.setApproval(Approval.REFUSAL);
                commonAsset.setDemandCheck(true);
                commonAssetRepository.save(commonAsset);
            }
        }

        return ResponseEntity.ok("Delete successful");
    }

    @PostMapping("/demandAction")
    public ResponseEntity<String> demandAction(@RequestBody List<DemandActionDto> request){
        for (DemandActionDto demandActionDto : request) {

            DemandHistoryDto actionData = demandActionDto.getDemandAction(); // actionData 배열
            String reason = demandActionDto.getReason(); // reason 값
            String actionType = demandActionDto.getActionType();

            if (!actionType.equals("hold")) {
                if (actionData.getDemandType().equals("update")) {


                    CommonAsset commonAsset = commonAssetRepository.findById(actionData.getAssetNo()).orElse(null);
                    DemandDtl demandDtl = demandDtlRepository.findByAssetNo(commonAsset);
                    demandDtl.setComment(reason);
                    demandDtlRepository.save(demandDtl);

                    if (commonAsset != null) {
                        if (Objects.equals(actionType, "approve")) {
                            commonAsset.setApproval(Approval.APPROVE);
                            commonAsset.setDemandCheck(true);
                            commonAssetRepository.save(commonAsset);
                            demandService.beforeDemand(actionData.getAssetCode(), actionData.getAssetNo());
                        } else {
                            commonAsset.setApproval(Approval.REFUSAL);
                            commonAsset.setDemandCheck(true);
                            commonAssetRepository.save(commonAsset);
                        }
                    }

                } else if (actionData.getDemandType().equals("delete")) {


                    CommonAsset commonAsset = commonAssetRepository.findById(actionData.getAssetNo()).orElse(null);
                    DemandDtl demandDtl = demandDtlRepository.findByAssetNo(commonAsset);
                    demandDtl.setComment(reason);
                    demandDtlRepository.save(demandDtl);
                    if (commonAsset != null) {
                        if (Objects.equals(actionType, "approve")) {
                            commonAsset.setApproval(Approval.APPROVE);
                            commonAsset.setDisposalStatus(true);
                            commonAsset.setDemandCheck(true);
                            commonAssetRepository.save(commonAsset);
                            demandService.beforeDemand(actionData.getAssetCode(), actionData.getAssetNo());
                        } else {
                            commonAsset.setApproval(Approval.REFUSAL);
                            commonAsset.setDemandCheck(true);
                            commonAssetRepository.save(commonAsset);
                        }
                    }

                } else {

                }

            } else {

            }
        }
        return ResponseEntity.ok("Action successful");
    }

}
