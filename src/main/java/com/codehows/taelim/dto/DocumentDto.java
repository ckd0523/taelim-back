package com.codehows.taelim.dto;

import com.codehows.taelim.constant.DocumentGrade;
import com.codehows.taelim.constant.DocumentType;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Document;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long documentNo;

    private CommonAsset assetNo;

    private DocumentGrade documentGrade;

    private DocumentType documentType;

    private String documentLink;

    // DTO에서 엔티티로 변환
    public Document toEntity() {
        return Document.builder()
                .documentNo(this.documentNo)
                .assetNo(this.assetNo)
                .documentGrade(this.documentGrade)
                .documentType(this.documentType)
                .documentLink(this.documentLink)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static DocumentDto fromEntity(Document entity) {
        return new DocumentDto(
                entity.getDocumentNo(),
                entity.getAssetNo(),
                entity.getDocumentGrade(),
                entity.getDocumentType(),
                entity.getDocumentLink()
        );
    }
}
