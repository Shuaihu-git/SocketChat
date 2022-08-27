import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ClientMG {


    //实现管理类的单例化
    private static final ClientMG clientMG = new ClientMG();
    public ClientMG() {
    }
    public static ClientMG getClientMG(){
        return clientMG;
    }
    //操作图形化界面
    ClientForm cWin;
    ClientChat cChat;
    public void setClientForm(ClientForm cf){
        cWin = cf;
    }
    //    设置界面中的消息记录
    public void setLog(String str){

        cWin.txtLog.append(str+"\r\n");

    }
    public ClientChat getClientChat(){
        return cChat;
    }
    //新上线的用户添加到JList中
    public void addItem(String username){
        cWin.items.addElement(username);
    }
    public void addItems(String [] sItems){
        for (String username : sItems) {
            addItem(username);
        }
    }
    //一旦断开连接，清空JList中的用户
    //清空单个
    public void removeItem(String str){
        cWin.items.removeElement(str);
    }
    //清空所有
    public void clearItems(){
        cWin.items.clear();
    }
    public void connect(String IP,int port,String Username){
        try {
            Socket socket = new Socket(IP,port);
            ClientMG.getClientMG().setLog("已连接到服务器 ");
            cChat = new ClientChat(socket,Username);
            cChat.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    FileListener filelistener;
    //创建文件传输的服务器  
    public String CreateFileTranServer(File file){
        try {
            ServerSocket server = new ServerSocket(9999);
            String IP = InetAddress.getLocalHost().getHostAddress();
            filelistener = new FileListener(server,file);
            filelistener.start();
            return IP+"|"+9999;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
    //接收文件
    public void recFile(String sender,String fName,int len,String IP,int port){
        //文件名|文件长度|IP|Port
        //4判断是否要接收？  JOptionPane->confirm
        //5同意接收，连接服务器进行文件传输
        //6拒绝接收，向服务器发回拒接接收的消息：FILECANCEL|sendername|recname

        JOptionPane msg = new JOptionPane();
        int result = msg.showConfirmDialog(null, sender+"发送文件【"+fName+"】,\r\n是否接收？", "文件传输确认", JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_NO_OPTION){
            //确认接收
            try {
                Socket socket = new Socket(IP,port);
                new fileRec(socket, fName, len).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else{
            //如果拒收   FILECANCEL|FromUser|toUser,sender给我发消息，某某某拒绝了，就通知sender，某某某拒绝了你的文件传输
            String strSend = "FILECANCEL|"+this.getClientChat().UserName+"|"+sender;
            this.getClientChat().sendMSG(strSend);
            this.setLog("您已拒接了"+sender+"的文件传输");
        }

    }
    //对方取消文件传递
    public void cancelFileTrans(){
        filelistener.cancelFileTrans();
        this.setLog("对方取消了文件传输！！！");
    }

}