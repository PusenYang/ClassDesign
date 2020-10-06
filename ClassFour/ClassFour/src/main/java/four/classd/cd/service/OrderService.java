package four.classd.cd.service;

import com.github.pagehelper.PageInfo;
import four.classd.cd.model.entity.DesignOrder;
import four.classd.cd.model.entity.UserOrder;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:40
 */
public interface OrderService {

    // 创建物资单
    boolean createUserOrder(UserOrder userOrder);

    // 物资单详情
    UserOrder getInfo(int id);

    UserOrder getInfo(String number);

    // 获取我的物资单
    PageInfo<UserOrder> getMyUserOrder(int userId, int pageNum, int size);

    // 个人用户确认已发出物资
    boolean userVerifySend(String number);

    // 调配站确认收到物资
    boolean designVerifyGet(String number);

    // 调配站确认已发出物资
    boolean designVerifySend(String number);

    // 接收站确认已收到物资
    boolean receiveVerifySend(String number);

    // 获取归属于某调配站的物资单
    PageInfo<UserOrder> getUserOrderByDesign(int designId, int pageNum, int size);

    // 创建调配单
    boolean createDesignOrder(DesignOrder designOrder);

    DesignOrder getInfo2(int id);

    DesignOrder getInfo2(String number);

    // 获取始发于某调配站的物资单
    PageInfo<DesignOrder> getDesignOrder(int designId, int pageNum, int size);

    // 获取归属于某接收站的物资单
    PageInfo<DesignOrder> getDesignOrderByReceive(int receiveId, int pageNum, int size);
}
