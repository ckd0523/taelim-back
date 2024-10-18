package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Furniture;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.entity.Software;
import com.codehows.taelim.secondEntity.TestMember;
import com.codehows.taelim.secondRepository.TestMemberRepository;
import com.codehows.taelim.repository.*;
import com.codehows.taelim.security.PasswordHasher2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DataInitializerService {

    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final SoftwareRepository softwareRepository;
    private final FurnitureRepository furnitureRepository;
    private final AmountSetRepository amountSetRepository;
    private final TestMemberRepository testMemberRepository;
    private final PasswordHasher2 passwordHasher2;

    @Transactional
    public void insertDummyData() {
        //자산 기준 금액 설정 초기값 설정
        amountSetRepository.insertAmountSet(0L, 0L);

        //두 번째 DB 테스트1
        TestMember testMember = new TestMember();
        testMember.toEntity("user1@example.com", "1234" ,
                "testUser1", Department.IT_DEPARTMENT.toString(), Role.ADMIN.toString(), true);
        testMemberRepository.save(testMember);

        //두 번째 DB 로그인을 위한 유저 등록 테스트2
        TestMember testMember2 = new TestMember();
        String hashedPassword = null;
        try {
            hashedPassword = passwordHasher2.hashPasswordV3("taelim");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        testMember2.toEntity("taelim@taelim.com", hashedPassword, "전찬용", Department.IT_DEPARTMENT.toString(), Role.ADMIN.toString(), true);
        testMemberRepository.save(testMember2);

        //두 번째 DB 로그인을 위한 유저 등록 테스트3
        TestMember testMember3 = new TestMember();
        String hashedPassword3 = null;
        try {
            hashedPassword3 = passwordHasher2.hashPasswordV3("taelim123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        testMember3.toEntity("taelim123@taelim.com", hashedPassword3, "이창현", Department.IT_DEPARTMENT.toString(), Role.ASSET_MANAGER.toString(), false);
        testMemberRepository.save(testMember3);


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
