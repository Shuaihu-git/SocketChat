import java.io.*;
import java.net.*;

public class FileListener extends Thread {
    ServerSocket fserver;
    File filetrans;
    public FileListener(ServerSocket fserver, File filetrans) {
        this.fserver = fserver;
        this.filetrans = filetrans;
    }
    //取消传输
    public void cancelFileTrans(){
        if(fserver!=null){
            try {
                fserver.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void run() {
        Socket sfile = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            sfile = fserver.accept();
            dis = new DataInputStream(new FileInputStream(filetrans));
            dos = new DataOutputStream(sfile.getOutputStream());

            //有关进度条属性的设置
            ClientMG.getClientMG().cWin.FileprogressBar.setValue(0);
            ClientMG.getClientMG().cWin.FileprogressBar.setVisible(true);
            ClientMG.getClientMG().cWin.FileprogressBar.setMaximum((int)filetrans.length());


            byte[] buff = new byte[1024];
            int iread = 0;
            int len =0;
            while((iread=dis.read(buff, 0, 1024))!=-1){
                dos.write(buff, 0, iread);  //写文件是，是根据读出的大小进行写入的
                len += iread;
                dos.flush();

            //写入流的同时，设置进度条的进度
                ClientMG.getClientMG().cWin.FileprogressBar.setValue(len);

            }
            ClientMG.getClientMG().setLog("文件传输完毕！");
            ClientMG.getClientMG().cWin.FileprogressBar.setValue(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
                dis.close();
                if(sfile!=null){sfile.close();}
                if(fserver!=null){fserver.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}