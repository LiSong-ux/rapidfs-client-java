package net.industryhive.client.service;

import net.industryhive.client.config.ConfigLoader;
import net.industryhive.client.protocol.BaseProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * @Author 未央
 * @Create 2020-10-23 14:22
 */
public class Contactor {
    private static final Random random = new Random();

    public static String contact(byte command) {
        Socket trackerSocket = null;
        DataOutputStream trackerDOS = null;
        DataInputStream trackerDIS = null;
        String storageAddr = null;
        try {
            int index = random.nextInt(ConfigLoader.TRACKER_ADDR.length);
            InetAddress address = InetAddress.getByName(ConfigLoader.TRACKER_ADDR[index]);
            trackerSocket = new Socket(address, 19093);
            trackerDOS = new DataOutputStream(trackerSocket.getOutputStream());
            byte[] header = getHeader(command);
            trackerDOS.write(header);
            trackerDIS = new DataInputStream(trackerSocket.getInputStream());
            storageAddr = trackerDIS.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (trackerDIS != null) {
                    trackerDIS.close();
                }
                if (trackerDOS != null) {
                    trackerDOS.close();
                }
                if (trackerSocket != null) {
                    trackerSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storageAddr;
    }

    public static String contact(String filePath) {
        Socket trackerSocket = null;
        DataOutputStream trackerDOS = null;
        DataInputStream trackerDIS = null;
        String storageAddr = null;
        try {
            trackerSocket = new Socket(InetAddress.getByName("127.0.0.1"), 19093);
            trackerDOS = new DataOutputStream(trackerSocket.getOutputStream());
            byte[] header = getHeader(BaseProtocol.DOWNLOAD_COMMAND);
            trackerDOS.write(header);
            trackerDOS.writeUTF(filePath);
            trackerDIS = new DataInputStream(trackerSocket.getInputStream());
            storageAddr = trackerDIS.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (trackerDIS != null) {
                    trackerDIS.close();
                }
                if (trackerDOS != null) {
                    trackerDOS.close();
                }
                if (trackerSocket != null) {
                    trackerSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storageAddr;
    }

    public static byte[] getHeader(byte command) {
        byte[] header = new byte[8];
        System.arraycopy(BaseProtocol.RECOGNIZE, 0, header, 0, 7);
        header[7] = command;
        return header;
    }
}
