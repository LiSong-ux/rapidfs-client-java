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

import static net.industryhive.client.util.IOUtil.close;

/**
 * @author LiSong-ux
 * @create 2021-07-24 17:20
 */
public class Downloader {
    public static DataInputStream download(String filepath) {
        DataInputStream dis = null;
        Socket storageSocket = null;
        RapidMessage contactMsg = Contactor.contact(filepath);
        if (contactMsg.getStatus() != 200) {
            System.out.println(contactMsg.getResult());
            return null;
        }
        try {
            InetAddress address = InetAddress.getByName(contactMsg.getResult());
            int port = Integer.parseInt(ConfigLoader.STORAGE_PORT);
            storageSocket = new Socket(address, port);

            DataOutputStream dos = new DataOutputStream(storageSocket.getOutputStream());
            dis = new DataInputStream(storageSocket.getInputStream());
            dos.write(Contactor.getHeader((byte) 1, BaseProtocol.DOWNLOAD_COMMAND));
            dos.writeUTF(filepath);
            if (dis.read() != 210) {
                String errorMsg = dis.readUTF();
                System.out.println(errorMsg);
                close(dis);
                close(storageSocket);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            close(dis);
            close(storageSocket);
            return null;
        }
        return dis;
    }
}
