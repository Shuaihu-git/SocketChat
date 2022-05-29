import java.net.*;
import java.io.*;
public class SocketChat extends Thread {

    Socket socket;
    BufferedReader br=null;
    PrintWriter pw = null;
    String UserName;
    public SocketChat(Socket socket) {
        this.socket = socket;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    "UTF-8"));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMSG(String str){
        pw.println(str);
        pw.flush();
    }
    //关闭SocketChat
    public void closeChat(){
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            String str = "";
            while((str = br.readLine())!=null){
                String comm[] = str.split("[|]");
                if(comm[0].equals("Login")){
                    //读取客户端发过来的用户名
                    String cUsername = comm[1];
                    //赋给服务器端的UserName
                    UserName = cUsername;
                    //①得到所有在线用户发给客户端
                    SocketMG.getsocketMG().sendOnlineUsers(this);
                    //②将当期的socket信息存放在OnLineUsers数组中
                    SocketMG.getsocketMG().addList(this);
                    //③把当前用户信息发送给在线用户
                    SocketMG.getsocketMG().sendNewUsertoAll(this);
                }else if(comm[0].equals("MSG")){
                    //交互协议 "MSG|"+sendUsername+"|"+targetUsername+"|"+sMSG
                    //取出协议串中的内容
                    String sender = comm[1];
                    String receiver = comm[2];
                    String msg = comm[3];
                    System.out.println("发送者："+sender+"---接收者："+receiver+"---消息体："+msg);
                    if(receiver.equals("ALL")){
                        //"MSG|"+sendUsername+"|ALL|"+msg
                        String strmsg = "MsgReturn|"+sender+"|"+msg;
                        SocketMG.getsocketMG().sendMSGToALL(strmsg, this);
                        SocketMG.getsocketMG().setLog(sender+"发消息给所有人"+"内容为："+msg);
                    }else{
                        //查询在线集合，如果接收对象则返回这个SocketChat对象
                        SocketChat sc = SocketMG.getsocketMG().getSocketChatByName(receiver);
                        System.out.println("选中的用户的信息："+sc);
                        if(!(sc==null)){
                            String strmsg = "MsgReturn"+"|"+sender+"|"+msg; //重新组织交换协议
                            System.out.println(strmsg);
                            SocketMG.getsocketMG().sendMSGToSocket(strmsg, sc);
                            SocketMG.getsocketMG().setLog(sender+"发送给"+sc+"的消息是："+msg);
                        }
                    }
                }else if(comm[0].equals("OFFLINE")){
                    //用户下线的交互协议："OFFLINE|"+username
                    //向其他用户发送该用户下线的消息
                    SocketMG.getsocketMG().sendOffLineMSGToAll(this);
                    //移除List中已经下线用户的信息
                    SocketMG.getsocketMG().removeList(this);
                    SocketMG.getsocketMG().setLog(comm[1]+"下线了");
                } else if(comm[0].equals("FILETRANS")){
                    // "FILETRANS|"+sender+"|"+sTarget+"|"+sFname+"|"+finfo.length()+"|"+IPandPort
                    String sender = comm[1];
                    SocketChat sTarget = SocketMG.getsocketMG().getSocketChatByName(comm[2]);
                    //sFname+"|"+finfo.length()+"|"+IPandPort
                    String strSend = comm[3]+"|"+comm[4]+"|"+comm[5]+"|"+comm[6];
                    //调用文件发送函数
                    SocketMG.getsocketMG().sendFileTrans(sender, sTarget, strSend);
                    SocketMG.getsocketMG().setLog(sender+"发送了"+strSend+"给"+sTarget);
                } else if(comm[0].equals("FILECANCEL")){
                    //FILECANCEL|拒收者（A）|被拒收者（B）
                    //得到被拒收者
                    String A = comm[1];
                    SocketChat sc = SocketMG.getsocketMG().getSocketChatByName(comm[2]);
                    //重新组织交互协议  //FILECANCELReturn|拒收者A
                    String strSend = "FILECANCELReturn|"+A;
                    SocketMG.getsocketMG().setLog(A+"拒收了"+comm[2]+"传输的文件");
                    sc.sendMSG(strSend);
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