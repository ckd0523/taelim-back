package com.codehows.taelim.entity;

import com.codehows.taelim.constant.DocumentGrade;
import com.codehows.taelim.constant.DocumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Document {

    @Id
    @Column(name = "documentNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    @Enumerated(EnumType.STRING)
    private DocumentGrade documentGrade;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentLink;

}
