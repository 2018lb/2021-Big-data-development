import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;
import java.util.Vector;


public class codeArea extends JSplitPane {
    private spark database;

    public codeArea(spark db) {
        super();
        database = db;
        toolBar2 = new JToolBar();
        run_button = new JButton();
        stop_button = new JButton();
        scrollPane2 = new JScrollPane();
        code = new JTextArea();
        panel6 = new JPanel();
        code_textArea = new JTextArea();
        res_tabbed = new STabbedPane(true);
        scrollPane3 = new JScrollPane();
        res_m = new JTextArea();


        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setResizeWeight(0.8);

        //======== panel6 ========
        {
            panel6.setLayout(new BorderLayout());

            //======== toolBar2 ========
            {

                //---- run ----
                run_button.setText("\u8fd0\u884c");
                toolBar2.add(run_button);

                //---- stop ----
                stop_button.setText("\u505c\u6b62");
                toolBar2.add(stop_button);
            }
            panel6.add(toolBar2, BorderLayout.NORTH);

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(code_textArea);
            }
            panel6.add(scrollPane2, BorderLayout.CENTER);
        }
        setTopComponent(panel6);
        setBottomComponent(res_tabbed);


        res_m.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        res_m.setBackground(new Color(69, 73, 73));
        res_m.setEditable(false);
        res_m.setEnabled(false);
        scrollPane3.setViewportView(res_m);

        res_tabbed.addTab("\u9996\u9875", scrollPane3,false);
        add_act();
    }
    private int res_count = 0;
    private void add_act(){

        //???????????????????????????
        run_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                res_m.setText("");
                String CODE = code_textArea.getText();
                int n = CODE.length(), cnt = 0;
                try {
                    while (cnt < n) {

                        //???????????????????????????????????????
                        int last = CODE.indexOf(";", cnt);
                        if (last == -1) last = n;
                        String Executing_code = CODE.substring(cnt, last);
                        res_m.append(Executing_code);
                        ResultSet resultSet = database.work(Executing_code);
                        res_m.append("\n> ok!\n\n");
                        cnt = last + 1;

                        //?????????????????????????????????????????????
                        ResultSetMetaData rsm = resultSet.getMetaData();
                        int columnCount = rsm.getColumnCount();

                        //??????sql??????????????????????????????
                        if (columnCount == 0) continue;

                        DefaultTableModel model = new DefaultTableModel();
                        for (int i = 1; i <= columnCount; i++){
                            String ColumnName = rsm.getColumnName(i);
                            model.addColumn(ColumnName);
                        }

                        JTable cnt_res = new JTable(model);

                        int cnt_column = 1;
                        while (resultSet.next()) {

                            Vector<String> s = new Vector();
                            cnt_column++;
                            for (int i = 1; i <= columnCount; i++){
                                s.add(resultSet.getString(i));
                            }
                            model.addRow(s);
                        }

                        JScrollPane scrollPane4 = new JScrollPane();
                        scrollPane4.setViewportView(cnt_res);

                        //????????????????????????
                        scrollPane4.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {

                                Dimension containerwidth = null;
                                if (!true) {
                                    // ???????????????????????????????????????????????????????????????0
                                    containerwidth = scrollPane4.getPreferredSize();
                                } else {
                                    // ???????????????????????????????????????????????????????????????????????????????????????
                                    containerwidth = scrollPane4.getSize();
                                }
                                // ???????????????????????? getTable().
                                int allwidth = cnt_res.getIntercellSpacing().width;
                                for (int j = 0; j < cnt_res.getColumnCount(); j++) {
                                    // ??????????????????????????????
                                    int max = 0;
                                    for (int i = 0; i < cnt_res.getRowCount(); i++) {
                                        int width = cnt_res
                                                .getCellRenderer(i, j)
                                                .getTableCellRendererComponent(cnt_res,
                                                        cnt_res.getValueAt(i, j), false, false, i, j)
                                                .getPreferredSize().width;
                                        if (width > max) {
                                            max = width;
                                        }
                                    }
                                    // ?????????????????????
                                    int headerwidth = cnt_res
                                            .getTableHeader()
                                            .getDefaultRenderer()
                                            .getTableCellRendererComponent(
                                                    cnt_res,
                                                    cnt_res.getColumnModel().getColumn(j)
                                                            .getIdentifier(), false, false, -1, j)
                                            .getPreferredSize().width;
                                    // ??????????????????????????????
                                    max += headerwidth;
                                    // ????????????
                                    cnt_res.getColumnModel().getColumn(j).setPreferredWidth(max);
                                    // ??????????????????????????????????????????????????????????????????????????????1?????????
                                    allwidth += max + cnt_res.getIntercellSpacing().width;
                                }
                                allwidth += cnt_res.getIntercellSpacing().width;
                                // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
                                if (allwidth > containerwidth.width) {
                                    cnt_res.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                } else {
                                    cnt_res.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                                }
                            }
                        });

                        res_count++;
                        res_tabbed.addTab("??????"+res_count, scrollPane4,true);

                        //??????50ms????????????????????????
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }

                    }
                }catch (Exception e1){
                    res_m.append("???????????????\n" + e1 +"\n");
                }
            }
        });
    }


    private JPanel panel6;
    private JToolBar toolBar2;
    private JButton run_button;
    private JButton stop_button;
    private JScrollPane scrollPane2;
    private JTextArea code;
    private JTextArea code_textArea;
    private STabbedPane res_tabbed;
    private JScrollPane scrollPane3;
    private JTextArea res_m;
}
