package com.s11web.service;

import com.s11web.dataPersistence.BarcodeGenerateDao;
import com.s11web.model.BarcodeGenerateInfo;
import com.s11web.model.BarcodeGenerateRecord;
import com.s11web.util.DecimalConversion;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarcodeGenerateService {

    @Autowired
    private BarcodeGenerateDao barcodeGenerateDao;

    private static final Logger log = Logger.getLogger(BarcodeGenerateService.class);

    public List<String[]> barcodeGenerateByCondition(String cargoType,
                                                     String source,
                                                     String destination,
                                                     int number,
                                                     String username) {
        //使用String[]方便生成Excel
        ArrayList<String[]> barcodeList = new ArrayList<>();

        try {
            if (cargoType.equals("MLPS")) {

                String preNumber;
                List<String> daoResult = barcodeGenerateDao.getPreNumberByCondition(cargoType, source, destination);

                if (daoResult.size() > 1) {
                    log.error("Multiple records exist!");
                    return barcodeList;
                }

                if (daoResult.size() == 1) {
                    preNumber = daoResult.get(0);
                } else {
                    preNumber = "00000";
                    BarcodeGenerateInfo barcodeGenerateInfo = new BarcodeGenerateInfo();
                    barcodeGenerateInfo.setCargoType(cargoType);
                    barcodeGenerateInfo.setSourceFC(source);
                    barcodeGenerateInfo.setDestinationFC(destination);
                    barcodeGenerateInfo.setCurrentNumber(preNumber);
                    barcodeGenerateDao.saveGenerateInfo(barcodeGenerateInfo);
                }

                for (int i = 0; i < number; ++i) {
                    barcodeList.add(new String[]{String.format("%s%s%s", source, destination, preNumber)});
                    preNumber = DecimalConversion.addOne(new StringBuffer(preNumber)).toString();
                }

                BarcodeGenerateInfo barcodeGenerateInfo = new BarcodeGenerateInfo();
                barcodeGenerateInfo.setCargoType(cargoType);
                barcodeGenerateInfo.setSourceFC(source);
                barcodeGenerateInfo.setDestinationFC(destination);
                barcodeGenerateInfo.setCurrentNumber(preNumber);
                barcodeGenerateDao.updatePreNumberByCondition(barcodeGenerateInfo);

                log.info(String.format("Barcode generate done! %s %s to %s sum %d", cargoType, source, destination, number));

                BarcodeGenerateRecord barcodeGenerateRecord = new BarcodeGenerateRecord();
                barcodeGenerateRecord.setUserName(username);
                barcodeGenerateRecord.setCargoType(cargoType);
                barcodeGenerateRecord.setSourceFC(source);
                barcodeGenerateRecord.setDestinationFC(destination);
                barcodeGenerateRecord.setNumber(number);
                barcodeGenerateRecord.setStartBarcode(barcodeList.get(0)[0]);
                barcodeGenerateRecord.setEndBarcode(barcodeList.get(barcodeList.size() - 1)[0]);
                barcodeGenerateDao.saveGenerateRecord(barcodeGenerateRecord);

                log.info(String.format("Barcode generate recode: %s %s %s %s %d %s %s",
                        barcodeGenerateRecord.getUserName(), cargoType, source, destination,
                        number, barcodeGenerateRecord.getStartBarcode(), barcodeGenerateRecord.getEndBarcode()));
            } else {
                log.error("暂不支持的业务!");
            }
        } catch (Exception e) {
            log.error(e);
        }

        return barcodeList;
    }

}
