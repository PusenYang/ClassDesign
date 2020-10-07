package four.classd.cd.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import four.classd.cd.dao.DesignStationDao;
import four.classd.cd.dao.ReceiveStationDao;
import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.enums.StationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 18:01
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Override
    public PageInfo<DesignStation> findAllDesign(int pageNum, int size) {
        Page<DesignStation> p = PageHelper.startPage(pageNum, size);
        designStationDao.findAlls();
        return new PageInfo<>(p);
    }

    @Override
    public PageInfo<DesignStation> findUncheckedDesign(int pageNum, int size) {
        Page<DesignStation> p = PageHelper.startPage(pageNum, size);
        designStationDao.findByStatus(StationStatus.UN_CHECK.getCode());
        return new PageInfo<>(p);
    }

    @Override
    public PageInfo<DesignStation> findCheckedDesign(int pageNum, int size) {
        Page<DesignStation> p = PageHelper.startPage(pageNum, size);
        designStationDao.findByStatus(StationStatus.CHECKED.getCode());
        return new PageInfo<>(p);
    }

    @Override
    public boolean checkDesign(int status, int id) {
        return designStationDao.updateStatus(status, id) >= 0;
    }

    @Override
    public boolean checkReceive(int status, int id) {
        return receiveStationDao.updateStatus(status, id) >= 0;
    }

    @Override
    public PageInfo<ReceiveStation> findAllReceive(int pageNum, int size) {
        Page<ReceiveStation> p = PageHelper.startPage(pageNum, size);
        receiveStationDao.findAlls();
        return new PageInfo<>(p);
    }

    @Override
    public PageInfo<ReceiveStation> findUncheckedReceive(int pageNum, int size) {
        Page<ReceiveStation> p = PageHelper.startPage(pageNum, size);
        receiveStationDao.findByStatus(StationStatus.UN_CHECK.getCode());
        return new PageInfo<>(p);
    }

    @Override
    public PageInfo<ReceiveStation> findCheckedReceive(int pageNum, int size) {
        Page<ReceiveStation> p = PageHelper.startPage(pageNum, size);
        receiveStationDao.findByStatus(StationStatus.CHECKED.getCode());
        return new PageInfo<>(p);
    }
}
