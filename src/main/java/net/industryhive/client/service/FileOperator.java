package net.industryhive.client.service;

import net.industryhive.client.config.ConfigLoader;
import net.industryhive.client.entity.RapidMessage;
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
    public static String upload(String fileName, FileInputStream fis) {
        if (fis == null) {
            System.out.println("FileInputStream Is Null");
            return null;
        }
        Socket storageSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        String filePath = null;
        try {
            RapidMessage contactMsg = Contactor.contact(BaseProtocol.UPLOAD_COMMAND);
            if (contactMsg.getStatus() != 200) {
                System.out.println(contactMsg.getResult());
                return null;
            }
            System.out.println(contactMsg.getResult());
            storageSocket = new Socket(InetAddress.getByName(contactMsg.getResult()), Integer.parseInt(ConfigLoader.STORAGE_PORT));
            dos = new DataOutputStream(storageSocket.getOutputStream());
            dos.write(Contactor.getHeader((byte) 1, BaseProtocol.UPLOAD_COMMAND));
            dos.writeUTF(fileName);
            byte[] fileBytes = new byte[1024];
            int length;
            while ((length = fis.read(fileBytes)) != -1) {
                dos.write(fileBytes, 0, length);
            }
            storageSocket.shutdownOutput();
            dis = new DataInputStream(storageSocket.getInputStream());
            int status = dis.read();
            String result = dis.readUTF();
            if (status != 200) {
                System.out.println(result);
                return null;
            }
            filePath = result;
            System.out.println("Upload Success: " + filePath);
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
        return filePath;
    }

    public static DataInputStream download(String filePath) {
        Socket storageSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            RapidMessage contactMsg = Contactor.contact(filePath);
            if (contactMsg.getStatus() != 200) {
                System.out.println(contactMsg.getResult());
                return null;
            }
            storageSocket = new Socket(InetAddress.getByName(contactMsg.getResult()), Integer.parseInt(ConfigLoader.STORAGE_PORT));
            dos = new DataOutputStream(storageSocket.getOutputStream());
            dis = new DataInputStream(storageSocket.getInputStream());
            dos.write(Contactor.getHeader((byte) 1, BaseProtocol.DOWNLOAD_COMMAND));
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
                if (storageSocket != null) {
                    storageSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dis;
    }
}