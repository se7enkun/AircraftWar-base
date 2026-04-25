package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import edu.hitsz.record.*;

public class ScoreTable {
    private JPanel mainPanel;
    private JTable scoreTable;
    private JButton deleteButton;
    private JScrollPane tableScrollPanel;
    private JLabel headerLabel;

    private RecordDao recordDao;
    private DefaultTableModel model;

    public ScoreTable(RecordDao recordDao) {
        this.recordDao = recordDao;

        headerLabel.setText("排行榜--难度：" + Main.difficulty.toLowerCase());



        // 字体美化
        headerLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));

        String[] columnName = {"名次", "玩家名", "得分", "记录时间"};
        List<Record> allRecords = recordDao.getAllRecords();
        String[][] tableData = new String[allRecords.size()][4];

        for (int i = 0; i < allRecords.size(); i++) {
            Record r = allRecords.get(i);
            tableData[i][0] = String.valueOf(i + 1);
            tableData[i][1] = r.getUserName();
            tableData[i][2] = String.valueOf(r.getScore());
            tableData[i][3] = r.getTime();
        }

        // 初始化表格模型
        model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        scoreTable.setModel(model);
        tableScrollPanel.setViewportView(scoreTable);

        // 删除按钮监听
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                if (row != -1) {
                    int result = JOptionPane.showConfirmDialog(deleteButton, "是否确定删除选中记录？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (JOptionPane.YES_OPTION == result) {
                        model.removeRow(row);
                        // 在内存和文件中同步删除该记录 (需在 RecordDaoImpl 补充 delete 方法)
                        recordDao.getAllRecords().remove(row);
                        recordDao.saveRecords(); // 保证数据持久化
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "请先选择一条记录！");
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
