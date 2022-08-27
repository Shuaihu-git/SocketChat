import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

public class fileRec extends Thread{
    Socket socket;
    //文件默认存储地址
    String sRecFilePath="D:\\";
    String fileName;
    int fileLen;
    public fileRec(Socket socket, String fileName, int fileLen) {
        this.socket = socket;
        this.fileName = fileName;
        this.fileLen = fileLen;
    }

    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        //设置进度条属性
        ClientMG.getClientMG().cWin.FileprogressBar.setValue(0);
        ClientMG.getClientMG().cWin.FileprogressBar.setVisible(true);
        ClientMG.getClientMG().cWin.FileprogressBar.setMaximum(fileLen);
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(new FileOutputStream(sRecFilePath+fileName));

            byte[] buff = new byte[1024];
            int iread = 0;
            int len = 0;
            while((iread=dis.read(buff, 0, 1024))!=-1){
                dos.write(buff, 0, iread);
                len +=iread;
                ClientMG.getClientMG().cWin.FileprogressBar.setValue(len);
                dos.flush();
            }
            ClientMG.getClientMG().setLog("文件接收完毕！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
                dos.close();
                if(socket!=null){ socket.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}