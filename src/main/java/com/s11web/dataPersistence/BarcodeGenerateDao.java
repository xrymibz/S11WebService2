package com.s11web.dataPersistence;

import com.s11web.model.BarcodeGenerateInfo;
import com.s11web.model.BarcodeGenerateRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeGenerateDao {

    List<String> getPreNumberByCondition(@Param(value = "cargoType") String cargoType,
                                         @Param(value = "source") String source,
                                         @Param(value = "destination") String destination);

    void saveGenerateInfo(BarcodeGenerateInfo barcodeGenerateInfo);

    void updatePreNumberByCondition(BarcodeGenerateInfo barcodeGenerateInfo);

    void saveGenerateRecord(BarcodeGenerateRecord barcodeGenerateRecord);
}
