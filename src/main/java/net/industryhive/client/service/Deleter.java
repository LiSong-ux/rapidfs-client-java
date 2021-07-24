package net.industryhive.client.service;

import net.industryhive.client.config.ConfigLoader;
import net.industryhive.client.connect.Contactor;
import net.industryhive.client.entity.RapidMessage;
import net.industryhive.client.protocol.BaseProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author LiSong-ux
 * @create 2021-07-24 17:21
 */
public class Deleter {
    public static String delete(String filePath) {
        Socket storageSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        String message;
        try {
            RapidMessage contactMsg = Contactor.contact(filePath);
            if (contactMsg.getStatus() != 200) {
                System.out.println(contactMsg.getResult());
                message = "Delete File Failed: " + contactMsg.getResult();
                return message;
            }
            storageSocket = new Socket(InetAddress.getByName(contactMsg.getResult()), Integer.parseInt(ConfigLoader.STORAGE_PORT));
            dos = new DataOutputStream(storageSocket.getOutputStream());
            dis = new DataInputStream(storageSocket.getInputStream());
            dos.write(Contactor.getHeader((byte) 1, BaseProtocol.DELETE_COMMAND));
            dos.writeUTF(filePath);
            int status = dis.read();
            String result = dis.readUTF();
            if (status != 200) {
                message = "Delete File Failed: " + result;
                return message;
            }
            System.out.println("Delete Success: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
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
        return "Delete Success: " + filePath;
    }
}
