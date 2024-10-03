package com.codehows.taelim.dto;

import com.codehows.taelim.entity.ApplicationProgram;
import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProgramDto {
    private Long appNo;

    private CommonAsset assetNo;

    private String serviceScope;
    private String os;
    private String relatedDB;
    private String ip;
    private Long screenNumber;

    // DTO를 엔티티로 변환하는 메서드
    public ApplicationProgram toEntity() {
        ApplicationProgram applicationProgram = new ApplicationProgram();
        applicationProgram.setAppNo(this.appNo);
        applicationProgram.setAssetNo(this.assetNo);
        applicationProgram.setServiceScope(this.serviceScope);
        applicationProgram.setOs(this.os);
        applicationProgram.setRelatedDB(this.relatedDB);
        applicationProgram.setIp(this.ip);
        applicationProgram.setScreenNumber(this.screenNumber);
        return applicationProgram;
    }

    // 엔티티를 DTO로 변환하는 메서드
    public static ApplicationProgramDto fromEntity(ApplicationProgram applicationProgram) {
        ApplicationProgramDto dto = new ApplicationProgramDto();
        dto.setAppNo(applicationProgram.getAppNo());
        dto.setAssetNo(applicationProgram.getAssetNo());
        dto.setServiceScope(applicationProgram.getServiceScope());
        dto.setOs(applicationProgram.getOs());
        dto.setRelatedDB(applicationProgram.getRelatedDB());
        dto.setIp(applicationProgram.getIp());
        dto.setScreenNumber(applicationProgram.getScreenNumber());
        return dto;
    }
}
