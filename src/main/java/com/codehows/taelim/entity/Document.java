package com.codehows.taelim.entity;

import com.codehows.taelim.constant.DocumentGrade;
import com.codehows.taelim.constant.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
