import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ServerForm extends JFrame {

    private JPanel contentPane;
    public JPanel panel;
    public JLabel label;
    public JTextField txtPort;
    public JButton btnStart;
    public JButton btnStop;
    public JScrollPane scrollPane;
    public JTextArea txtLog;


    ServerSocket server;
    ServerListener listener;
    volatile boolean serverFlag;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerForm frame = new ServerForm();
                    frame.setVisible(true);
                    SocketMG.getsocketMG().setServerForm(frame); //窗体对象传入SocketMG中
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ServerForm() {
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 422, 520);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "\u914D\u7F6E\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(0, 0, 406, 66);
        contentPane.add(panel);
        panel.setLayout(null);

        label = new JLabel("\u7AEF\u53E3\uFF1A");
        label.setBounds(10, 29, 54, 15);
        panel.add(label);

        txtPort = new JTextField();
        txtPort.setText("2020");
        txtPort.setBounds(46, 21, 66, 30);
        panel.add(txtPort);
        txtPort.setColumns(10);

        btnStart = new JButton("\u5F00\u59CB\u670D\u52A1");
        btnStart.addActionListener(new BtnStartActionListener());
        btnStart.setBounds(129, 21, 98, 31);
        panel.add(btnStart);

        btnStop = new JButton("\u505C\u6B62\u670D\u52A1");
        btnStop.addActionListener(new BtnStopActionListener());
        btnStop.setBounds(253, 21, 98, 31);
        panel.add(btnStop);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder(null, "\u6D88\u606F\u8BB0\u5F55", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        scrollPane.setBounds(0, 76, 406, 406);
        contentPane.add(scrollPane);

        txtLog = new JTextArea();
        scrollPane.setViewportView(txtLog);
    }

    //启动Socket服务
    private class BtnStartActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            int port = Integer.parseInt(txtPort.getText().trim());
            try {

                server = new ServerSocket(port);
                listener = new ServerListener(server);
                listener.start();
                SocketMG.getsocketMG().setLog("服务已开启");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //停止服务
    private class BtnStopActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            SocketMG.getsocketMG().setLog("服务器已关闭");
            //通知所有在线用户服务器关闭消息    交互协议为："CLOSE|"
            SocketMG.getsocketMG().sendCloseMSGToAll();
            //把OnlineUsers集合中的每一个SocketChat关闭
            SocketMG.getsocketMG().closeALLSocket();
            //ArrayList清空
            SocketMG.getsocketMG().clearList();
            //关闭ServerSocket
            listener.stopListener();

            try {
                if (server != null) {
                    serverFlag = false;
                    server.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
