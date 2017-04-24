package com.s11web.service;

import com.s11web.dao.AuxiliaryDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xietian
 * 2017/4/18.
 */
@Service
public class AuxiliaryService {
    private static final Logger log = Logger.getLogger(AuxiliaryService.class);
    @Autowired
    private AuxiliaryDao auxiliaryDao;
    public List<String> getScanIDbyDate(String Date){
        return auxiliaryDao.getScanIDbyDate(Date);
    }
    public void uodateScanInfo(String ScanID,String PV,String PW,String Box){
        log.debug("UpdateScanInfo is beginning  ---------");
        auxiliaryDao.uodateScanInfo(ScanID,PV,PW,Box);
        log.debug("UpdateScanInfo is finished  ---------");
    }
}
