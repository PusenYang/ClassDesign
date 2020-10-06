package four.classd.cd.service;

import four.classd.cd.model.entity.ReceiveStation;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/6 21:40
 */
public interface StationService {

    List<ReceiveStation> getNearStation(int designStationId);

    String genAdvice(int designStationId);
}
