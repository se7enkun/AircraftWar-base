package edu.hitsz.record;

import java.util.List;

public interface RecordDao {
    /**
     * 获取所有得分记录
     */
    List<Record> getAllRecords();

    /**
     * 新增一条得分记录
     */
    void doAdd(Record record);


    void saveRecords();
    // 指导书中提到了删除记录，这里预留接口，供后续界面交互时使用
    // void doDelete(int index);
}