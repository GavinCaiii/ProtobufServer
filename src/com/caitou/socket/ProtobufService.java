package com.caitou.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-06-24.
 */
public class ProtobufService implements Runnable{
    private DataReceived mListener;
    private Socket mConnect;
    private TransBean mBean;

    @Override
    public void run() {
        if (mConnect == null)
            return;
        try {
            InputStream is = mConnect.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Object obj = ois.readObject();
            if (!(obj instanceof TransBean))
                return;
            mBean = (TransBean)obj;
            mListener.onReceived(mBean);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ProtobufService(Socket connect, DataReceived listener){
        mConnect = connect;
        mListener = listener;
    }


    /**
     * On command received, this interface will be call
     * @author GuangzhaoCai
     * @since 2016-06-24
     */
    public interface DataReceived {
        void onReceived(TransBean bean);
    }
}
