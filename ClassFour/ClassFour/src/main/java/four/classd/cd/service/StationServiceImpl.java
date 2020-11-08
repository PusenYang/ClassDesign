package four.classd.cd.service;

import four.classd.cd.dao.DesignStationDao;
import four.classd.cd.dao.ReceiveStationDao;
import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.util.GaodeMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/6 21:40
 */
@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Autowired
    private DesignStationDao designStationDao;

    // 全局变量
    private int r1 = 0;
    private int r2 = 0;
    private int r3 = 0;

    @Override
    public List<ReceiveStation> getNearStation(int designStationId) {
        DesignStation designStation = designStationDao.findById(designStationId);
        List<ReceiveStation> stationAll = receiveStationDao.findAlls();
        for (ReceiveStation rs : stationAll) {
            rs.setDistance(GaodeMapUtil.getDistance(designStation.getAddress(), rs.getAddress()));
        }
        // 排序
        // Collections.sort(stationAll, (rs1, rs2)->rs1.getDistance().compareTo());
        stationAll.sort((rs1, rs2)->rs1.getDistance().compareTo(rs2.getDistance()));
        return stationAll;
    }

    @Override
    public String genAdvice(int designStationId) {
        List<ReceiveStation> stations = getNearStation(designStationId);
        if (stations.size() < 1) return "附近没有接收站, 请自由发挥";
        // int cnt = 0;
        for (ReceiveStation rs : stations) {
            // 不缺乏物资, 跳过
            if (! checkLack(rs.getId())) {
                // cnt ++;
                break;
            }
            // 对于缺乏物资的站点
            else {
                return getLackAmount(designStationId, rs.getId());
            }
        }
        return "附近的接收站并不缺乏物资, 请自由发挥";
    }

    private String getLackAmount(int did, int rid) {
        // 处理n95
        String res = "当前距离您站最近的是: "+receiveStationDao.findNameById(rid);
        if (r1 < 0) {
            res += "; 该站缺乏N95口罩"+(-r1)+"个, 您站拥有N95口罩"+designStationDao.getAmount(did, ResourceType.N95.getCode()) + "个";
        }
        if (r2 < 0) {
            res += "; 该站缺乏PM2.5口罩"+(-r2)+"个, 您站拥有PM2.5口罩"+designStationDao.getAmount(did, ResourceType.PM25.getCode()) + "个";
        }
        if (r3 < 0) {
            res += "; 该站缺乏普通医用外科口罩"+(-r3)+"个, 您站拥有普通医用外科口罩"+designStationDao.getAmount(did, ResourceType.Ori.getCode()) + "个";
        }
        return res + "; 请您优先考虑该站😊";
    }

    /**
     * 检验接收站是否缺乏物资
     * @param id
     * @return
     */
    private boolean checkLack(int id) {
        ReceiveStation rs = receiveStationDao.findById(id);
        r1 = receiveStationDao.getAmount(id, ResourceType.N95.getCode());
        r2 = receiveStationDao.getAmount(id, ResourceType.PM25.getCode());
        r3 = receiveStationDao.getAmount(id, ResourceType.Ori.getCode());
        return r1 < 0 || r2 < 0 || r3 < 0;
    }
}
