package edu.hitsz.record;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.hitsz.application.Main.difficulty;

public class RecordDaoImpl implements RecordDao {
    private List<Record> records;
    private String filePath;

    public RecordDaoImpl(String difficulty) {
        this.records = new ArrayList<>();
        // 暂时写死难度对应的文件
        this.filePath = "scores_" + difficulty + ".txt";
        loadRecords();
    }

    //从文件中加载历史记录到内存中
    private void loadRecords() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    records.add(new Record(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将内存中的记录写回文件持久化
    public void saveRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Record record : records) {
                bw.write(record.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Record> getAllRecords() {
        return records;
    }

    @Override
    public void doAdd(Record record) {
        records.add(record);
        // 每次添加新记录后，重新排序
        Collections.sort(records);
        saveRecords();
    }
}