package com.caitou;

import com.caitou.socket.ProtobufService;
import com.caitou.socket.SocketServer;
import com.caitou.socket.TransBean;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SocketServer.getInstance().startToListen(new ProtobufService.DataReceived() {
            @Override
            public void onReceived(TransBean bean) {
                String data = new String(bean.data);
                System.out.println("data = " + data);

                //返回到客户端
                SocketServer.getInstance().sendDataToClient(bean);
            }
        });
    }
}
