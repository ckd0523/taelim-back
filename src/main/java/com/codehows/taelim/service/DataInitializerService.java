package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Furniture;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.entity.Software;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.FurnitureRepository;
import com.codehows.taelim.repository.MemberRepository;
import com.codehows.taelim.repository.SoftwareRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DataInitializerService {

    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final SoftwareRepository softwareRepository;
    private final FurnitureRepository furnitureRepository;

    @Transactional
    public void insertDummyData() {
        // Member 데이터 삽입
        for (int i = 1; i <= 40; i++) {
            Member member = new Member();
            member.setEmail("user" + i + "@example.com");
            member.setPassword("password" + i);
            member.setUName("User Name " + i);
            member.setRole(i % 10 == 0 ? Role.ADMIN : (i % 2 == 0 ? Role.ASSET_MANAGER : Role.USER));
            memberRepository.save(member);
        }

        // CommonAsset 첫번째 데이터 삽입
        for (int i = 1; i <= 40; i++) {

            Member member = new Member();
            member.setEmail("user" + i + "@example.com");
            member.setPassword("password" + i);
            member.setUName("User Name " + i);
            member.setRole(Role.USER);

            CommonAsset asset = new CommonAsset();
            if (i <= 5) {
                asset.setAssetClassification(AssetClassification.SOFTWARE);
            }else {
                asset.setAssetClassification(AssetClassification.FURNITURE);
            }
            asset.setAssetBasis(AssetBasis.COMMON);
            asset.setAssetCode(String.format("ASSET%03d", i));
            asset.setAssetName("Asset " + i);
            asset.setPurpose("Test Purpose");
            asset.setQuantity(1L);
            asset.setDepartment(Department.IT_DEPARTMENT);
            asset.setAssetLocation(AssetLocation.MAIN_1F);
            asset.setAssetUser(member);
            asset.setAssetOwner(member);
            asset.setAssetSecurityManager(member);
            asset.setOperationStatus(OperationStatus.OPERATING);
            asset.setIntroducedDate(LocalDate.now());
            asset.setConfidentiality(1);
            asset.setIntegrity(1);
            asset.setAvailability(1);
            asset.setNote("Test Note");
            asset.setManufacturingCompany("Test Manufacturing Company");
            asset.setOwnership(Ownership.OWNED);
            asset.setPurchaseCost(5000L);
            asset.setPurchaseDate(LocalDate.now());
            asset.setUsefulLife(5L);
            asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
            asset.setWarrantyDetails("Test Warranty Details");
            asset.setAttachment("Test Attachment");
            asset.setPurchaseSource("Test Purchase Source");
            asset.setContactInformation("010-0000-0000");
            asset.setDisposalStatus(Boolean.FALSE);
            asset.setDemandStatus(Boolean.FALSE);
            asset.setApproval(Approval.APPROVE);
            asset.setDemandCheck(Boolean.FALSE);
            asset.setCreateDate(LocalDate.now());
            asset.setUseState(UseState.IN_USE);
            asset.setAcquisitionRoute("Test Acquisition Route");
            asset.setMaintenancePeriod(LocalDate.now());
            // 기타 필드 설정
            commonAssetRepository.save(asset);
            if(i<=5) {
                Software software = new Software();
                software.setAssetNo(asset);
                software.setIp("192.168.1." + i);
                software.setServerId("server" + String.format("%02d", i));
                software.setServerPassword("pass" + i);
                software.setCompanyManager("Manager " + i);
                software.setOs("Windows Server 2022");
                softwareRepository.save(software);
            } else {
                Furniture furniture = new Furniture();
                furniture.setAssetNo(asset);
                furniture.setFurnitureSize("500");
                furnitureRepository.save(furniture);
            }
        }

        //수정
//        for (int i = 1; i <= 10; i++) {
//            if(i%3==0) {
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(Role.USER);
//
//                CommonAsset asset = new CommonAsset();
//                asset.setAssetClassification(AssetClassification.FURNITURE);
//                asset.setAssetBasis(AssetBasis.COMMON);
//                asset.setAssetCode(String.format("ASSET%03d", i));
//                asset.setAssetName("Asset " + i);
//                asset.setPurpose("Test Purpose");
//                asset.setQuantity(1L);
//                asset.setDepartment(Department.IT_DEPARTMENT);
//                asset.setAssetLocation(AssetLocation.MAIN_1F);
//                asset.setAssetUser(member);
//                asset.setAssetOwner(member);
//                asset.setAssetSecurityManager(member);
//                asset.setOperationStatus(OperationStatus.OPERATING);
//                asset.setIntroducedDate(LocalDate.now());
//                asset.setConfidentiality(1);
//                asset.setIntegrity(1);
//                asset.setAvailability(1);
//                asset.setNote("Test Note");
//                asset.setManufacturingCompany("Test Manufacturing Company");
//                asset.setOwnership(Ownership.OWNED);
//                asset.setPurchaseCost(5000L);
//                asset.setPurchaseDate(LocalDate.now());
//                asset.setUsefulLife(5L);
//                asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
//                asset.setWarrantyDetails("Test Warranty Details");
//                asset.setAttachment("Test Attachment");
//                asset.setPurchaseSource("Test Purchase Source");
//                asset.setContactInformation("010-0000-0000");
//                asset.setDisposalStatus(Boolean.FALSE);
//
//                asset.setDemandStatus(Boolean.TRUE);
//                if (i < 5) {
//                    asset.setApproval(Approval.APPROVE);
//                } else {
//                    asset.setApproval(Approval.REFUSAL);
//                }
//
//                asset.setDemandCheck(Boolean.TRUE);
//                asset.setCreateDate(LocalDate.now());
//                asset.setUseState(UseState.IN_USE);
//                asset.setAcquisitionRoute("Test Acquisition Route");
//                asset.setMaintenancePeriod(LocalDate.now());
//                // 기타 필드 설정
//                commonAssetRepository.save(asset);
//                if(i<=5) {
//                    Software software = new Software();
//                        software.setAssetNo(asset);
//                        software.setIp("192.168.1." + i);
//                        software.setServerId("server" + String.format("%02d", i));
//                        software.setServerPassword("pass" + i);
//                        software.setCompanyManager("Manager " + i);
//                        software.setOs("Windows Server 2022");
//                        softwareRepository.save(software);
//                    } else {
//                    Furniture furniture = new Furniture();
//                        furniture.setAssetNo(asset);
//                        furniture.setFurnitureSize("500");
//                        furnitureRepository.save(furniture);
//                }
//            }
//        }

        //폐기
//        for (int i = 1; i <= 10; i++) {
//            if(i%5==0) {
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(Role.USER);
//
//                CommonAsset asset = new CommonAsset();
//                asset.setAssetClassification(AssetClassification.FURNITURE);
//                asset.setAssetBasis(AssetBasis.COMMON);
//                asset.setAssetCode(String.format("ASSET%03d", i));
//                asset.setAssetName("Asset " + i);
//                asset.setPurpose("Test Purpose");
//                asset.setQuantity(1L);
//                asset.setDepartment(Department.IT_DEPARTMENT);
//                asset.setAssetLocation(AssetLocation.MAIN_1F);
//                asset.setAssetUser(member);
//                asset.setAssetOwner(member);
//                asset.setAssetSecurityManager(member);
//                asset.setOperationStatus(OperationStatus.OPERATING);
//                asset.setIntroducedDate(LocalDate.now());
//                asset.setConfidentiality(1);
//                asset.setIntegrity(1);
//                asset.setAvailability(1);
//                asset.setNote("Test Note");
//                asset.setManufacturingCompany("Test Manufacturing Company");
//                asset.setOwnership(Ownership.OWNED);
//                asset.setPurchaseCost(5000L);
//                asset.setPurchaseDate(LocalDate.now());
//                asset.setUsefulLife(5L);
//                asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
//                asset.setWarrantyDetails("Test Warranty Details");
//                asset.setAttachment("Test Attachment");
//                asset.setPurchaseSource("Test Purchase Source");
//                asset.setContactInformation("010-0000-0000");
//                asset.setQRInformation("Test QR Information");
//                asset.setDisposalStatus(Boolean.TRUE);
//                asset.setRequestStatus(Boolean.TRUE);
//                if (i < 8) {
//                    asset.setApproval(Approval.APPROVE);
//                    asset.setUseState(UseState.RETIRED_DISCARDED);
//                } else {
//                    asset.setApproval(Approval.REFUSAL);
//                    asset.setUseState(UseState.IN_USE);
//                }
//                asset.setDemandCheck(Boolean.TRUE);
//                asset.setCreateDate(LocalDate.now());
//                asset.setAcquisitionRoute("Test Acquisition Route");
//                asset.setMaintenancePeriod(LocalDate.now());
//                // 기타 필드 설정
//                commonAssetRepository.save(asset);
//            }
//        }

    }
}



//if(i<=50) {
//Software software = new Software();
//                software.setAssetNo(asset);
//                software.setIP("192.168.1." + i);
//                software.setServerId("server" + String.format("%02d", i));
//        software.setServerPassword("pass" + i);
//                software.setCompanyManager("Manager " + i);
//                software.setOS("Windows Server 2022");
//                softwareRepository.save(software);
//            } else {
//Furniture furniture = new Furniture();
//                furniture.setAssetNo(asset);
//                furniture.setFurnitureSize("500");
//            }
