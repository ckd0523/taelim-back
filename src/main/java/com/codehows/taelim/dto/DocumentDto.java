package com.codehows.taelim.dto;

import com.codehows.taelim.constant.DocumentGrade;
import com.codehows.taelim.constant.DocumentType;
import com.codehows.taelim.entity.CommonAsset;
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
}
