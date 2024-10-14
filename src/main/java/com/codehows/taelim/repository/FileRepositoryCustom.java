package com.codehows.taelim.repository;

import com.codehows.taelim.entity.File;

import java.util.List;

public interface FileRepositoryCustom {
    List<File> findByAssetCode(String assetCode);

}
