import java.io.*;
import java.net.*;

public class ClientChat extends Thread {

    Socket socket;
    BufferedReader br = null;
    PrintWriter pw = null;
    String UserName;

    public ClientChat(Socket socket, String userName) {
        this.socket = socket;
        UserName = userName;
        try {

            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    "UTF-8"));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        //约定一个登录的交互协议Login|username
        sendMSG("Login|"+UserName);
    }
    public void sendMSG(String str){
        pw.println(str);
        pw.flush();
    }
    //断开socket连接
    public void closeChat(){
        try {
            if(socket!=null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //处理服务器发过来的在线用户List ，交互协议为：USERLISTS|user1_user2_user3
    public void run() {
        try {
            String str = "";
            while((str = br.readLine())!=null){
                System.out.println("---"+str+"---");
                String comms[] = str.split("[|]");
                if(comms[0].equals("USERLISTS")){
                    String users[] = comms[1].split("_");
                    ClientMG.getClientMG().addItems(users);
                } else if(comms[0].equals("ADD")){
                    ClientMG.getClientMG().addItem(comms[1]);
                } else if(comms[0].equals("MsgReturn")){
                    //"MsgReturn|"+sender+"|"+msg
                    String sender = comms[1];
                    String msg = comms[2];
                    ClientMG.getClientMG().setLog("【"+sender+"】：");
                    ClientMG.getClientMG().setLog(msg);
                } else if(comms[0].equals("DEL")){
                    //交互协议为："DEL|"+UserName
                    String sUser = comms[1];
                    ClientMG.getClientMG().removeItem(sUser);
                    ClientMG.getClientMG().setLog(sUser+"已下线");
                } else if(comms[0].equals("CLOSE")){
                    ClientMG.getClientMG().setLog("服务器已关闭");
                    //关闭ClientChat
                    ClientMG.getClientMG().getClientChat().closeChat();
                    //清空在线用户列表
                    ClientMG.getClientMG().clearItems();
                } else if(comms[0].equals("FILETRANS")){
                    //"FILETRANS|"+sender+"|"+sFname+"|"+finfo.length()+"|"+IP+"|"+Port
                    String sender = comms[1];
                    String fileName = comms[2];
                    int filelen = Integer.parseInt(comms[3]);
                    String sIP = comms[4];
                    int port = Integer.parseInt(comms[5]);
                    //调用ClientMG中的接收文件的方法
                    ClientMG.getClientMG().recFile(sender, fileName, filelen, sIP, port);

                } else if(comms[0].equals("FILECANCEL")){
                    ClientMG.getClientMG().cancelFileTrans();
                } else if (comms[0].equals("FILECANCELReturn")){
                    //FILECANCELReturn|拒收者A
                    ClientMG.getClientMG().setLog(comms[1]+"取消了传输。。。");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null)
                    br.close();
                if (pw != null)
                    pw.close();
                if (socket != null)
                    socket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}