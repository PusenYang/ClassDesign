package four.classd.cd.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import four.classd.cd.dao.DesignOrderDao;
import four.classd.cd.dao.UserOrderDao;
import four.classd.cd.model.entity.DesignOrder;
import four.classd.cd.model.entity.UserOrder;
import four.classd.cd.model.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 22:05
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserOrderDao userOrderDao;

    @Autowired
    private DesignOrderDao designOrderDao;

    @Override
    public boolean createUserOrder(UserOrder userOrder) {
        return userOrderDao.addOrder(userOrder) >= 0;
    }

    @Override
    public UserOrder getInfo(int id) {
        return userOrderDao.findById(id);
    }

    @Override
    public UserOrder getInfo(String number) {
        return userOrderDao.findByNumber(number);
    }

    @Override
    public PageInfo<UserOrder> getMyUserOrder(int userId, int pageNum, int size) {
        Page<UserOrder> p = PageHelper.startPage(pageNum, size);
        userOrderDao.findByUser(userId);
        return new PageInfo<>(p);
    }

    @Override
    public boolean userVerifySend(String number) {
        return userOrderDao.updateStatusByNumber(OrderStatus.DE_UNRECEIVE.getCode(), number) >= 0;
    }

    @Override
    public boolean designVerifyGet(String number) {
        return userOrderDao.updateStatusByNumber(OrderStatus.DE_RECEIVE.getCode(), number) >= 0;
    }

    @Override
    public boolean designVerifySend(String number) {
        return userOrderDao.updateStatusByNumber(OrderStatus.GOING.getCode(), number) >= 0;
    }

    @Override
    public boolean receiveVerifySend(String number) {
        return userOrderDao.updateStatusByNumber(OrderStatus.FINAL_RECEIVE.getCode(), number) >= 0;
    }

    @Override
    public PageInfo<UserOrder> getUserOrderByDesign(int designId, int pageNum, int size) {
        Page<UserOrder> p = PageHelper.startPage(pageNum, size);
        userOrderDao.findByDesign(designId);
        return new PageInfo<>(p);
    }

    @Override
    public boolean createDesignOrder(DesignOrder designOrder) {
        return designOrderDao.addOrder(designOrder) >= 0;
    }

    @Override
    public DesignOrder getInfo2(int id) {
        return designOrderDao.findById(id);
    }

    @Override
    public DesignOrder getInfo2(String number) {
        return designOrderDao.findByNumber(number);
    }

    @Override
    public PageInfo<DesignOrder> getDesignOrder(int designId, int pageNum, int size) {
        Page<DesignOrder> p = PageHelper.startPage(pageNum, size);
        designOrderDao.findByDesign(designId);
        return new PageInfo<>(p);
    }

    @Override
    public PageInfo<DesignOrder> getDesignOrderByReceive(int receiveId, int pageNum, int size) {
        Page<DesignOrder> p = PageHelper.startPage(pageNum, size);
        designOrderDao.findByReceive(receiveId);
        return new PageInfo<>(p);
    }

}
