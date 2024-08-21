package com.codehows.taelim.entity;

import com.codehows.taelim.constant.documentGrade;
import com.codehows.taelim.constant.documentType;
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
public class document {

    @Id
    @Column(name = "documentNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private documentGrade documentGrade;
    private documentType documentType;
    private String documentLink;

}
