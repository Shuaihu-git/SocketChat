import java.io.IOException;
import java.net.*;


public class ServerListener extends Thread {

    ServerSocket server;
    //服务器状态
    volatile boolean serverFlag;
    public ServerListener(ServerSocket server) {
        this.server = server;
        serverFlag = true;
    }
    //停止监听
    public void stopListener(){
        serverFlag = false;
    }
    public void run() {
        while(serverFlag){
            Socket s = null;
            try {

                s = server.accept();
                new SocketChat(s).start();
                SocketMG.getsocketMG().setLog(s+"已登录");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}