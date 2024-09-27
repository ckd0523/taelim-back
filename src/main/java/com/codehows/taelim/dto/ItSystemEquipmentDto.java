package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ItSystemEquipment;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItSystemEquipmentDto {
    private Long equipmentNo;

    private CommonAsset assetNo;

    private String equipmentType;
    private Long rackUnit;
    private String powerSupply;
    private String coolingSystem;
    private String interfacePorts;
    private String formFactor;
    private Long expansionSlots;
    private String graphicsCard;
    private String portConfiguration;
    private Boolean monitorIncluded;

    // DTO에서 엔티티로 변환
    public ItSystemEquipment toEntity() {
        return ItSystemEquipment.builder()
                .equipmentNo(this.equipmentNo)
                .assetNo(this.assetNo)
                .equipmentType(this.equipmentType)
                .rackUnit(this.rackUnit)
                .powerSupply(this.powerSupply)
                .coolingSystem(this.coolingSystem)
                .interfacePorts(this.interfacePorts)
                .formFactor(this.formFactor)
                .expansionSlots(this.expansionSlots)
                .graphicsCard(this.graphicsCard)
                .portConfiguration(this.portConfiguration)
                .monitorIncluded(this.monitorIncluded)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static ItSystemEquipmentDto fromEntity(ItSystemEquipment entity) {
        return new ItSystemEquipmentDto(
                entity.getEquipmentNo(),
                entity.getAssetNo(),
                entity.getEquipmentType(),
                entity.getRackUnit(),
                entity.getPowerSupply(),
                entity.getCoolingSystem(),
                entity.getInterfacePorts(),
                entity.getFormFactor(),
                entity.getExpansionSlots(),
                entity.getGraphicsCard(),
                entity.getPortConfiguration(),
                entity.getMonitorIncluded()
        );
    }
}
