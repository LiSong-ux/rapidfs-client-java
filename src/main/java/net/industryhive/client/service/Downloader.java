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
 * @create 2021-07-24 17:20
 */
public class Downloader {
    public static DataInputStream download(String filepath) {
        Socket storageSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            RapidMessage contactMsg = Contactor.contact(filepath);
            if (contactMsg.getStatus() != 200) {
                System.out.println(contactMsg.getResult());
                return null;
            }
            storageSocket = new Socket(InetAddress.getByName(contactMsg.getResult()), Integer.parseInt(ConfigLoader.STORAGE_PORT));
            dos = new DataOutputStream(storageSocket.getOutputStream());
            dis = new DataInputStream(storageSocket.getInputStream());
            dos.write(Contactor.getHeader((byte) 1, BaseProtocol.DOWNLOAD_COMMAND));
            dos.writeUTF(filepath);
            if (dis.read() != 210) {
                String errorMsg = dis.readUTF();
                System.out.println(errorMsg);
                return null;
            }
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
