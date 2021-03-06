import java.awt.*;
import java.awt.event.*;

import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

import java.sql.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/*
 * Created by JFormDesigner on Wed Jun 09 14:34:03 GMT+08:00 2021
 */


/**
 * @author 1
 */
public class Main extends JFrame {
    public Main() {
        initComponents();
        int query_count = 0;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        MainWindow = new JFrame();
        hollarea = new JSplitPane();
        scrollPane1 = new JScrollPane();
        database_tree = new JTree();
        panel1 = new JPanel();
        work_tabbed = new STabbedPane();
        scrollPane3 = new JScrollPane();
        workArea = new JTextArea();
        toolBar1 = new JToolBar();
        connect_button = new JButton();
        query_button = new JButton();
        connect_dialog = new JDialog();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        IP = new JTextField();
        DB = new JTextField();
        USER = new JTextField();
        PASSWORD = new JTextField();
        PORT = new JTextField();
        connect_confirm = new JButton();
        codeArea = new JSplitPane();
        panel6 = new JPanel();
        toolBar2 = new JToolBar();
        run = new JButton();
        stop = new JButton();
        scrollPane2 = new JScrollPane();
        code_textArea = new JTextArea();
        res_tabbed = new JTabbedPane();

        //======== MainWindow ========
        {
            Container MainWindowContentPane = MainWindow.getContentPane();
            MainWindowContentPane.setLayout(new BorderLayout());

            //======== hollarea ========
            {
                hollarea.setResizeWeight(0.2);
                //======== scrollPane1 ========
                {

                    //---- database_tree ----
                    //database_tree.setPreferredSize(new Dimension(180, 82));
                    scrollPane1.setViewportView(database_tree);
                    scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane1.setViewportBorder(BorderFactory.createEtchedBorder());
                }
                hollarea.setLeftComponent(scrollPane1);

                //======== panel1 ========
                {
                    panel1.setLayout(new BorderLayout(0, 10));

                    //======== work_tabbed ========
                    {

                        //======== scrollPane3 ========
                        {

                            //---- workArea ----
                            workArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                            workArea.setBackground(new Color(69, 73, 73));
                            workArea.setEditable(false);
                            workArea.setEnabled(false);
                            scrollPane3.setViewportView(workArea);
                        }
                        work_tabbed.addTab("\u9996\u9875", scrollPane3,false);
                    }
                    panel1.add(work_tabbed, BorderLayout.CENTER);

                    //======== toolBar1 ========
                    {
                        toolBar1.setBackground(new Color(60, 73, 73));

                        //---- connect_button ----
                        connect_button.setText("\u8fde\u63a5\u6570\u636e\u5e93");
                        toolBar1.add(connect_button);

                        //---- query_button ----
                        query_button.setText("\u65b0\u5efa\u67e5\u8be2");
                        toolBar1.add(query_button);
                    }
                    panel1.add(toolBar1, BorderLayout.NORTH);
                }
                hollarea.setRightComponent(panel1);
            }
            MainWindowContentPane.add(hollarea, BorderLayout.CENTER);
            MainWindow.pack();
            MainWindow.setLocationRelativeTo(MainWindow.getOwner());
        }

        //======== connect_dialog ========
        {
            Container connect_dialogContentPane = connect_dialog.getContentPane();
            connect_dialogContentPane.setLayout(null);

            //---- label5 ----
            label5.setText("\u8fde\u63a5\u670d\u52a1\u5668");
            connect_dialogContentPane.add(label5);
            label5.setBounds(280, 20, 95, 50);

            //---- label6 ----
            label6.setText("\u4e3b\u673a");
            connect_dialogContentPane.add(label6);
            label6.setBounds(20, 105, 55, 35);

            //---- label7 ----
            label7.setText("\u7aef\u53e3");
            connect_dialogContentPane.add(label7);
            label7.setBounds(20, 157, 55, 35);

            //---- label8 ----
            label8.setText("\u521d\u59cb\u6570\u636e\u5e93");
            connect_dialogContentPane.add(label8);
            label8.setBounds(20, 209, 95, 35);

            //---- label9 ----
            label9.setText("\u7528\u6237\u540d");
            connect_dialogContentPane.add(label9);
            label9.setBounds(20, 261, 55, 35);

            //---- label10 ----
            label10.setText("\u5bc6\u7801");
            connect_dialogContentPane.add(label10);
            label10.setBounds(20, 313, 55, 35);
            connect_dialogContentPane.add(IP);
            IP.setBounds(140, 105, 435, 40);
            connect_dialogContentPane.add(DB);
            DB.setBounds(140, 207, 435, 40);
            connect_dialogContentPane.add(USER);
            USER.setBounds(140, 258, 185, 40);
            connect_dialogContentPane.add(PASSWORD);
            PASSWORD.setBounds(140, 309, 185, 40);
            connect_dialogContentPane.add(PORT);
            PORT.setBounds(140, 156, 120, 40);

            //---- connect_confirm ----
            connect_confirm.setText("\u8fde\u63a5");
            connect_dialogContentPane.add(connect_confirm);
            connect_confirm.setBounds(255, 385, 110, 35);

            connect_dialogContentPane.setPreferredSize(new Dimension(625, 485));
            connect_dialog.pack();
            connect_dialog.setLocationRelativeTo(connect_dialog.getOwner());
        }

        //======== codeArea ========
        {
            codeArea.setOrientation(JSplitPane.VERTICAL_SPLIT);
            codeArea.setResizeWeight(0.8);

            //======== panel6 ========
            {
                panel6.setLayout(new BorderLayout());

                //======== toolBar2 ========
                {

                    //---- run ----
                    run.setText("\u8fd0\u884c");
                    toolBar2.add(run);

                    //---- stop ----
                    stop.setText("\u505c\u6b62");
                    toolBar2.add(stop);
                }
                panel6.add(toolBar2, BorderLayout.NORTH);

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(code_textArea);
                }
                panel6.add(scrollPane2, BorderLayout.CENTER);
            }
            codeArea.setTopComponent(panel6);
            codeArea.setBottomComponent(res_tabbed);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame MainWindow;
    private JSplitPane hollarea;
    private JScrollPane scrollPane1;
    private JTree database_tree;
    private JPanel panel1;
    private STabbedPane work_tabbed;
    private JScrollPane scrollPane3;
    private JTextArea workArea;
    private JToolBar toolBar1;
    private JButton connect_button;
    private JButton query_button;
    private JDialog connect_dialog;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JTextField IP;
    private JTextField DB;
    private JTextField USER;
    private JTextField PASSWORD;
    private JTextField PORT;
    private JButton connect_confirm;
    private JSplitPane codeArea;
    private JPanel panel6;
    private JToolBar toolBar2;
    private JButton run;
    private JButton stop;
    private JScrollPane scrollPane2;
    private JTextArea code_textArea;
    private JTabbedPane res_tabbed;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("?????????");
    private String ip;
    private String port;
    private String db;
    private String user;
    private String password;
    private spark database;

    public void start(){

        set_dataTree();
        set_log();

        // ????????????
        add_act();
        //MainWindow.pack();
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setSize(new Dimension(800, 600));
        MainWindow.setVisible(true);
        //workArea.setText("");
    }

    private void set_log(){
        IP.setText("bigdata116.depts.bingosoft.net");
        PORT.setText("22116");
        DB.setText("user22_db");
        USER.setText("user22");
        PASSWORD.setText("pass@bingo22");
    }

    //??????JTree??????
    private void set_dataTree(){

        treeModel = new DefaultTreeModel(rootNode);

        database_tree.setModel(treeModel);
        //database_tree.setRootVisible(false);
        database_tree.setShowsRootHandles(true);
        database_tree.setEditable(false);

    }

    private int query_count;//????????????????????????
    private void add_act(){
        //???????????????????????????
        connect_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //??????????????????
                connect_dialog.pack();
                connect_dialog.setResizable(false);
                connect_dialog.setVisible(true);
            }
        });

        //????????????????????????
        connect_confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //??????????????????
                ip = IP.getText().trim();
                port = PORT.getText().trim();
                db = DB.getText().trim();
                user = USER.getText().trim();
                password = PASSWORD.getText().trim();
                connect_dialog.setVisible(false);

                //????????????
                workArea.append("> ???????????????" + ip + ':' + port + "???.....\n");
                int k = 1;
                try{
                    database = new spark(ip, port, db, user, password);
                }catch (Exception e1){
                    //workArea.append("???????????????" + ip + ':' + port + "??????\n");
                    workArea.append("> " + e1 + '\n');
                    workArea.append("> ???????????????" + ip + ':' + port + "??????\n\n\n");

                    //k = 0??????????????????
                    k = 0;
                    return;
                }finally {

                }
                workArea.append("\n\n");

                //?????????????????????????????????
                DefaultMutableTreeNode ipNode = new DefaultMutableTreeNode(ip);
                //DefaultMutableTreeNode dbNode = new DefaultMutableTreeNode(db);

                treeModel.insertNodeInto(ipNode, rootNode, rootNode.getChildCount());

                //treeModel.insertNodeInto(dbNode, ipNode, ipNode.getChildCount());
                DefaultMutableTreeNode _dataNode = new DefaultMutableTreeNode("?????????");
                treeModel.insertNodeInto(_dataNode, ipNode, ipNode.getChildCount());
                try {

                    //??????ip?????????????????????
                    ResultSet resultSet_db = database.showdb();
                    while(resultSet_db.next()) {
                        String dbName = resultSet_db.getString(1);
                        DefaultMutableTreeNode dbNode = new DefaultMutableTreeNode(dbName);

                        treeModel.insertNodeInto(dbNode, _dataNode, _dataNode.getChildCount());

                        if(dbName.equals(db)) {//??????????????????????????????
                            DefaultMutableTreeNode _tableNode = new DefaultMutableTreeNode("???");
                            treeModel.insertNodeInto(_tableNode, dbNode, dbNode.getChildCount());
                            //??????????????????????????????
                            ResultSet resultSet_table = database.showtable(dbName);

                            while (resultSet_table.next()) {

                                String tableName = resultSet_table.getString(1);
                                DefaultMutableTreeNode table_Node = new DefaultMutableTreeNode(tableName);

                                treeModel.insertNodeInto(table_Node, _tableNode, _tableNode.getChildCount());

                                DefaultMutableTreeNode _wordNode = new DefaultMutableTreeNode("??????");
                                treeModel.insertNodeInto(_wordNode, table_Node, table_Node.getChildCount());
                                //???????????????????????????
                                ResultSet resultSet_word = database.showword(tableName);
                                while (resultSet_word.next()) {
                                    String wordName = resultSet_word.getString(1);
                                    if (wordName.equals(""))  break;
                                    DefaultMutableTreeNode word_Node = new DefaultMutableTreeNode(wordName);

                                    treeModel.insertNodeInto(word_Node, _wordNode, _wordNode.getChildCount());
                                }
                            }
                        }
                    }
                    database_tree.updateUI();
                } catch (SQLException throwables) {
                    workArea.append("> ??????????????????????????????" + throwables+ "\n\n");
                }

                //????????????k=1??????????????????
                if (k == 1) workArea.append("> ???????????????" + ip + ':' + port + "??????\n\n");
            }
        });

        //????????????????????????
        query_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    database.test();
                    codeArea t = new codeArea(database);
                    query_count += 1;

                    //??????????????????
                    work_tabbed.addTab("????????????" + query_count, t, true);
                }
                catch(Exception a) {
                    workArea.append("> " + a + '\n');
                    workArea.append("> ?????????????????????\n\n");
                }
            }
        });

        //??????????????????????????????
        database_tree.addMouseListener(new MouseClickedTiwceListener() {
            @Override
            public void mouseClickedTwice(MouseEvent e) {
                super.mouseClickedTwice(e);
                TreePath selPath = database_tree.getPathForLocation(e.getX(), e.getY());
                if (selPath != null)// ?????????????????????!???????????????????????????
                {

                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    try{
                        //??????????????????????????????????????????????????????
                        if (node.getParent().toString().equals("?????????") && node.isLeaf()){

                            workArea.append("> ???????????????" + node.toString()+"...\n");

                            DefaultMutableTreeNode _tableNode = new DefaultMutableTreeNode("???");
                            treeModel.insertNodeInto(_tableNode, node, node.getChildCount());
                            String dbName = node.toString();
                            ResultSet resultSet_table = database.showtable(dbName);
                            while (resultSet_table.next()) {

                                String tableName = resultSet_table.getString(1);
                                DefaultMutableTreeNode table_Node = new DefaultMutableTreeNode(tableName);

                                treeModel.insertNodeInto(table_Node, _tableNode, _tableNode.getChildCount());
                            }
                            workArea.append("> ????????????\n\n");
                        }
                    }catch (Exception e1){
                        workArea.append(e1 + "\n\n");
                    }
                    //???????????????????????????????????????????????????????????????
                    if (node.getParent().getParent().toString().equals(db)){
                        try {

                            String Executing_code = "select * from " + node.toString() + " limit 500";
                            ResultSet resultSet; //
                            try{
                                resultSet = database.work(Executing_code);
                            }catch (Exception e1)
                            {
                                workArea.append(e1+"\n\n");
                                return;
                            }


                            ResultSetMetaData rsm = resultSet.getMetaData();
                            int columnCount = rsm.getColumnCount();

                            DefaultTableModel model = new DefaultTableModel();
                            for (int i = 1; i <= columnCount; i++) {
                                String ColumnName = rsm.getColumnName(i);
                                model.addColumn(ColumnName);
                            }

                            JTable cnt_res = new JTable(model);

                            while (resultSet.next()) {

                                Vector<String> s = new Vector();
                                for (int i = 1; i <= columnCount; i++) {
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


                            work_tabbed.addTab(node.toString(), scrollPane4, true);
                        }catch(Exception e1){
                            workArea.append(e1+"\n\n");
                        }
                    }
                }
            }
        });
    }


    public static void main(String[] args) {
        // ???????????? GUI
        Main k = new Main();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                k.start();
            }
        });
    }

    public static class MouseClickedTiwceListener extends MouseAdapter{
        private static  boolean flag = false;		//?????????????????????????????????
        private static int clickNum = 1;		//??????????????????????????????????????????
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            final MouseEvent me = e;
            MouseClickedTiwceListener.flag= false;
            if (MouseClickedTiwceListener.clickNum==2) {
                //?????????????????????2??????????????????
                this.mouseClickedTwice(me);
                //????????????clickNum??????1
                MouseClickedTiwceListener.clickNum=1;
                MouseClickedTiwceListener.flag=true;
                return;
            }
            //???????????????????????????????????????500ms
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                //???????????????????????????
                int num = 0;
                @Override
                public void run() {
                    // ????????????????????????????????????????????????
                    if(MouseClickedTiwceListener.flag) {
                        num=0;
                        MouseClickedTiwceListener.clickNum=1;
                        this.cancel();
                        return;
                    }
                    //????????????????????????????????????????????????????????????????????????
                    if (num==1) {
                        mouseClickedOnce(me);
                        MouseClickedTiwceListener.flag=true;
                        MouseClickedTiwceListener.clickNum=1;
                        num=0;
                        this.cancel();
                        return;
                    }
                    clickNum++;
                    num++;
                }
            },new Date(), 500);
        }
        protected void mouseClickedOnce(MouseEvent me) {
            // ????????????
            //System.out.println("1");
        }
        public void mouseClickedTwice(MouseEvent me) {
            // ????????????
            //("2");
        }

    }
}


