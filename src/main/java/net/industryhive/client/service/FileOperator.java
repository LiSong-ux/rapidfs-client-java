package net.industryhive.client.service;

import net.industryhive.client.protocol.BaseProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Author 未央
 * @Create 2020-10-29 11:20
 */
public class FileOperator {
    public static int upload(String fileName, FileInputStream fis) {
        if (fis == null) return 1;
        Socket storageSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            String storageAddr = Contactor.contact(BaseProtocol.UPLOAD_COMMAND);

            storageSocket = new Socket(InetAddress.getByName(storageAddr), 19094);
            dos = new DataOutputStream(storageSocket.getOutputStream());
            dos.write(Contactor.getHeader(BaseProtocol.UPLOAD_COMMAND));

            byte[] fileBytes = new byte[1024];
            dos.writeUTF(fileName);
            while (fis.read(fileBytes) != -1) {
                dos.write(fileBytes);
            }
            storageSocket.shutdownOutput();
            dis = new DataInputStream(storageSocket.getInputStream());
            int status = dis.read();
            String result = dis.readUTF();
            if (status != 200) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (storageSocket != null) {
                    storageSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static DataInputStream download(String filePath) {
        Socket socket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            String storageAddr = Contactor.contact(filePath);
            socket = new Socket(InetAddress.getByName(storageAddr), 19094);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            dos.write(Contactor.getHeader(BaseProtocol.DOWNLOAD_COMMAND));
            dos.writeUTF(filePath);
            int status = dis.read();
            String result = dis.readUTF();
            if (status != 200) {
                System.out.println(result);
                return null;
            }
            System.out.println("Download Success: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dis;
    }
}
