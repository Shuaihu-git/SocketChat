import java.util.ArrayList;

public class SocketMG {

    //实现管理类的单例化
    private static final SocketMG socketMG=new SocketMG();
    private SocketMG(){}
    public static SocketMG getsocketMG(){
        return socketMG;
    }
    SocketChat sChat;
    //操作图形界面
    ServerForm sWin;
    public void setServerForm(ServerForm s){
        sWin=s;
    }
    //设置界面中的消息记录
    public void setLog(String str){
        sWin.txtLog.append(str+"\r\n");
    }

    //存储socket信息的集合
    private ArrayList<SocketChat> OnLineUsers = new ArrayList<SocketChat>();
    //把新用户添加到集合中
    public synchronized void addList(SocketChat sChat){
        OnLineUsers.add(sChat);
    }
    //得到所有用户在线信息名称，发回给客户端
    public void sendOnlineUsers(SocketChat sc){
        //发送所有用户信息的协议：USERLISTS|user1_user2_user3
        if(OnLineUsers.size()>0){
            String str = "";
            for (int i = 0; i < OnLineUsers.size(); i++) {
                SocketChat sChat = OnLineUsers.get(i);
                str += sChat.UserName+"_";
            }
            sc.sendMSG("USERLISTS|"+str);
        }
    }
    //新上线的用户发送给所有在线用户 ADD|用户名
    public void sendNewUsertoAll(SocketChat sc){
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat socketChat = OnLineUsers.get(i);
            //新上线用户这个消息不发给自己，发给其他用户，让其他用户知道该你上线了
            if(!socketChat.equals(sc)){
                socketChat.sendMSG("ADD|"+sc.UserName);
            }
        }
    }
    //通过用户名查找SocketChat对象
    public SocketChat getSocketChatByName(String name){
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat sChat = OnLineUsers.get(i);
            //此处曾出现过错误，形式为：sChat.equals(name)
            if(sChat.UserName.equals(name)){
                return sChat;
            }
        }
        return null;
    }

    //向目标对象发送消息
    public void sendMSGToSocket(String str,SocketChat sc){
        System.out.println("sendMsgToSocket"+str);
        sc.sendMSG(str);
    }
    //发送消息出自己以外的所有人
    public void sendMSGToALL(String str,SocketChat sc){
        for (int i = 0; i < OnLineUsers.size(); i++) {
            //写程序的时候遇到的问题：SocketChat sChat = OnLineUsers.get(i);  
            //造成了与类成员变量重名，消息只发给自己
            SocketChat socketChat = OnLineUsers.get(i);
            if(!socketChat.equals(sc)){
                socketChat.sendMSG(str);
            }
            socketChat.sendMSG(str);
        }
    }
    //向其他用户发送下线用户下线的通知
    public void sendOffLineMSGToAll(SocketChat sc){
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat socketChat = OnLineUsers.get(i);
            if(!socketChat.equals("sc")){
                String str = "DEL|"+sc.UserName;
                socketChat.sendMSG(str);
            }
        }
    }
    //把当前下线的用户从OnLineUsers中清除
    public synchronized void removeList(SocketChat sChat){
        //清除ArrayList中的当前用户信息（socketChat）
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat schat=OnLineUsers.get(i);
            if(schat.equals(sChat)){
                OnLineUsers.remove(sChat);
                break;
            }
        }
    }
    //关闭服务器通知给所有在线用户
    public void sendCloseMSGToAll(){
        //组织交互协议为："CLOSE|"
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat socketchat = OnLineUsers.get(i);
            socketchat.sendMSG("CLOSE|");
        }
    }
    //关闭OnlineUsers中的每一个SocketChat
    public void closeALLSocket(){
        for (int i = 0; i < OnLineUsers.size(); i++) {
            SocketChat socketchat = OnLineUsers.get(i);
            socketchat.closeChat();
        }
    }
    //清空在线集合中的内容
    public void clearList(){
        OnLineUsers.clear();
    }
    //文件传输    发送者，接收者，内容
    public void sendFileTrans(String sender,SocketChat sTarget,String sMSG){
        sTarget.sendMSG("FILETRANS|"+sender+"|"+sMSG);
    }
}