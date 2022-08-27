
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ClientForm extends JFrame {

    private JPanel contentPane;
    public JPanel panel;
    public JLabel lblIp;
    public JTextField txtIP;
    public JLabel label_1;
    public JTextField txtPort;
    public JButton btnLogin;
    public JButton btnExit;
    public JPanel panel_1;
    public JButton btnSend;

    Socket socket;
    BufferedReader br=null;
    PrintWriter pw=null;
    public JScrollPane scrollPane;
    public JTextArea txtLog;
    public JScrollPane scrollPane_1;
    public JTextArea txtSend;
    public JLabel label;
    public JTextField txtUser;
    public JScrollPane scrollPane_2;
    public JList lOnlines;

    DefaultListModel<String> items=new DefaultListModel<String>();
    public JButton sendToAll;
    public JButton FileTranbutton;
    public JProgressBar FileprogressBar;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientForm frame = new ClientForm();
                    frame.setVisible(true);
                    ClientMG.getClientMG().setClientForm(frame);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 513, 583);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u767B\u5F55\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(11, 10, 476, 64);
        contentPane.add(panel);

        lblIp = new JLabel("IP:");
        lblIp.setHorizontalAlignment(SwingConstants.RIGHT);
        lblIp.setBounds(10, 19, 35, 31);
        panel.add(lblIp);

        txtIP = new JTextField();
        txtIP.setText("127.0.0.1");
        txtIP.setColumns(10);
        txtIP.setBounds(55, 22, 97, 24);
        panel.add(txtIP);

        label_1 = new JLabel("\u7AEF\u53E3:");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setBounds(154, 19, 35, 31);
        panel.add(label_1);

        txtPort = new JTextField();
        txtPort.setText("2020");
        txtPort.setColumns(10);
        txtPort.setBounds(190, 21, 35, 26);
        panel.add(txtPort);

        btnLogin = new JButton("\u767B\u5F55");
        btnLogin.addActionListener(new BtnLoginActionListener());
        btnLogin.setBounds(337, 22, 65, 25);
        panel.add(btnLogin);

        btnExit = new JButton("\u9000\u51FA");
        btnExit.addActionListener(new BtnExitActionListener());
        btnExit.setBounds(401, 22, 65, 25);
        panel.add(btnExit);

        label = new JLabel("\u7528\u6237\u540D:");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBounds(228, 19, 50, 31);
        panel.add(label);

        txtUser = new JTextField();
        txtUser.setText("visiter");
        txtUser.setColumns(10);
        txtUser.setBounds(281, 22, 50, 26);
        panel.add(txtUser);

        panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "\u64CD\u4F5C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(10, 414, 477, 121);
        contentPane.add(panel_1);

        btnSend = new JButton("\u53D1\u9001\u6D88\u606F");
        btnSend.addActionListener(new BtnSendActionListener());
        btnSend.setBounds(357, 82, 110, 23);
        panel_1.add(btnSend);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_1.setBounds(10, 24, 457, 48);
        panel_1.add(scrollPane_1);

        txtSend = new JTextArea();
        scrollPane_1.setViewportView(txtSend);

        sendToAll = new JButton("\u7FA4\u53D1\u6D88\u606F");
        sendToAll.addActionListener(new ButtonActionListener());
        sendToAll.setBounds(254, 82, 93, 23);
        panel_1.add(sendToAll);

        FileTranbutton = new JButton("\u4F20\u8F93\u6587\u4EF6");
        FileTranbutton.addActionListener(new ButtonActionListener_1());
        FileTranbutton.setBounds(10, 82, 81, 23);
        panel_1.add(FileTranbutton);

        FileprogressBar = new JProgressBar();
        FileprogressBar.setBounds(98, 92, 146, 8);
        panel_1.add(FileprogressBar);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder(null, "\u804A\u5929\u8BB0\u5F55", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        scrollPane.setBounds(11, 84, 309, 332);
        contentPane.add(scrollPane);

        txtLog = new JTextArea();
        scrollPane.setViewportView(txtLog);

        scrollPane_2 = new JScrollPane();
        scrollPane_2.setBorder(new TitledBorder(null, "\u5728\u7EBF\u7528\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        scrollPane_2.setBounds(323, 83, 164, 332);
        contentPane.add(scrollPane_2);

        lOnlines = new JList(items);
        scrollPane_2.setViewportView(lOnlines);

    }
    //登录
    private class BtnLoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            String ip = txtIP.getText().trim();
            int port = Integer.parseInt(txtPort.getText().trim());
            String socketname = txtUser.getText().trim();
            ClientMG.getClientMG().connect(ip, port, socketname);
        }
    }
    //发送信息
    private class BtnSendActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            //选中要聊天的用户
            String sendUsername = null;
            if(lOnlines.getSelectedIndex()>=0){
                //得到用户选择的名称
                String targetUsername = lOnlines.getSelectedValue().toString();
                System.out.println(targetUsername);
                //发送者的名称
                sendUsername = txtUser.getText().trim();
                //消息体
                String sMSG = txtSend.getText();
                //交互协议 "MSG|"+sendUsername+"|"+targetUsername+"|"+sMSG
                //包装后的消息发送出去
                String strSend = "MSG|"+sendUsername+"|"+targetUsername+"|"+sMSG;
                System.out.println(strSend);
                ClientMG.getClientMG().getClientChat().sendMSG(strSend);
                ClientMG.getClientMG().setLog("I send To "+targetUsername+":");
                ClientMG.getClientMG().setLog(sMSG);
                //清空发送消息框
                txtSend.setText("");
            }

        }
    }
    //退出操作
    private class BtnExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            //组织推出交互协议"OFFLINE|"+username
            String sendMSG = "OFFLINE|"+txtUser.getText().trim();
            ClientMG.getClientMG().getClientChat().sendMSG(sendMSG);
            //断开与服务器的socket连接
            ClientMG.getClientMG().getClientChat().closeChat();
            ClientMG.getClientMG().clearItems();  //清空列表

        }
    }
    private class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //发送者的名称
            String sendUsername = txtUser.getText().trim();
            //消息体
            String sMSG = txtSend.getText();
            //交互协议 "MSG|"+sendUsername+"|"+targetUsername+"|"+sMSG
            //包装后的消息发送出去
            String strSend = "MSG|"+sendUsername+"|ALL|"+sMSG;
            System.out.println(strSend);
            //协议为："MSG|"+sendUsername+"|ALL|"+msg
            ClientMG.getClientMG().getClientChat().sendMSG(strSend);
            txtSend.setText("");
        }
    }
    //传输文件
    private class ButtonActionListener_1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //1从本地选中一个文件，获取其文件相关信息（文件名，文件长度）
            //2创建文件传输的服务器，与服务器接通
            //3组织文件传输交互协议进行传输
            //FILETRANS|sendername|recname|文件名|文件长度|IP|Port

            File finfo = null;
            //用于文件选择的对象jFileChooser
            JFileChooser jFileChooser = new JFileChooser();
            int result = jFileChooser.showOpenDialog(null);
            if(result==JFileChooser.APPROVE_OPTION){
                finfo = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                String sFname = finfo.getName();
                //在用户列表中选择目标用户
                if(lOnlines.getSelectedIndex()!=-1){
                    String sTarget = lOnlines.getSelectedValue().toString();//得到目标用户
                    String sender = txtUser.getText().trim();
                    //得到新开服务器的IP+Port
                    String IPandPort = ClientMG.getClientMG().CreateFileTranServer(finfo);
                    //组织交互协议串，然后发送    FILETRANS+发送者+目标用户+文件名+文件的长度+IP和端口号
                    String strSend = "FILETRANS|"+sender+"|"+sTarget+"|"+sFname+"|"+finfo.length()+"|"+IPandPort;
                    ClientMG.getClientMG().getClientChat().sendMSG(strSend);
                }
            }

        }
    }
}