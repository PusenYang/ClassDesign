package four.classd.cd.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.enums.StationStatus;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 17:49
 */
public interface AdminService {

    // 所有调配站
    PageInfo<DesignStation> findAllDesign(int pageNum, int size);

    // 待审核的调配站
    PageInfo<DesignStation> findUncheckedDesign(int pageNum, int size);

    // 审核通过的调配站
    PageInfo<DesignStation> findCheckedDesign(int pageNum, int size);

    // 审核调配站
    boolean checkDesign(int status, int id);

    // 审核接收站
    boolean checkReceive(int status, int id);

    // 所有接收站
    PageInfo<ReceiveStation> findAllReceive(int pageNum, int size);

    // 待审核的接收站
    PageInfo<ReceiveStation> findUncheckedReceive(int pageNum, int size);

    // 审核通过的接收站
    PageInfo<ReceiveStation> findCheckedReceive(int pageNum, int size);
}
