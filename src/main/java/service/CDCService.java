package service;

import Models.CDC;
import dto.CDCRequest;

import java.util.List;
import java.util.UUID;

public interface CDCService {
    CDC createCDC(CDCRequest cdcRequest) throws Exception;
    CDC updateCDC(UUID id, CDCRequest cdcRequest) throws Exception;
    void deleteCDC(UUID id) throws Exception;
    CDC getCDCById(UUID id) throws Exception;
    List<CDC> getAllCDCs();
    List<CDC> searchCDCsByTitle(String title);
    List<CDC> getCDCsByType(String type);
}