package com.caitou.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @className: TransferService
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-06-24.
 */
public class TransferService implements Runnable{
    private DataReceived mListener;
    private Socket mConnect;

    @Override
    public void run() {
        if (mConnect == null)
            return;
        try {
            InputStream is = mConnect.getInputStream();
            while (true){
                byte[] buffer = new byte[1024];
                int ret;
                if ((ret = is.read(buffer)) != -1){
                    byte[] data = new byte[ret];
                    for (int i = 0; i < ret; i ++){
                        data[i] = buffer[i];
                    }
                    mListener.onReceived(data);
                } else
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TransferService(Socket connect, DataReceived listener){
        mConnect = connect;
        mListener = listener;
    }


    /**
     * On command received, this interface will be call
     * @author GuangzhaoCai
     * @since 2016-06-24
     */
    public interface DataReceived {
        void onReceived(byte[] data);
    }
}
