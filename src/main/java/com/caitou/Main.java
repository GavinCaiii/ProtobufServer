package com.caitou;

import com.caitou.data.BaseFrame;
import java.lang.String;
import com.caitou.protocol.Protocol;
import com.caitou.socket.TransferService;
import com.caitou.socket.SocketServer;
import com.caitou.utils.HexDump;
import com.google.protobuf.InvalidProtocolBufferException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SocketServer.getInstance().startToListen(new TransferService.DataReceived() {
            public void onReceived(byte[] data) {
                if (data != null){
                    Protocol.Frame frame = null;
                    String str = HexDump.dumpHexString(data);
                    System.out.print("==================================================");
                    System.out.println(str);
                    System.out.println("==================================================");

                    try {
                        frame = Protocol.Frame.parseFrom(data);
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }

                    int ctlCode = frame.getHeader().getControlCode();
                    int funCode = frame.getHeader().getFunctionCode();
                    System.out.println("==================================================");
                    System.out.println("ctlCode = " + ctlCode + "; funCode = " + funCode);
                    System.out.println("==================================================");
                    if (funCode == BaseFrame.FUNC_INT){
                        int int32 = frame.getRequest().getIntRequest().getInt32Data();
                        long int64 = frame.getRequest().getIntRequest().getInt64Data();
                        System.out.println("==================================================");
                        System.out.println("int32 = " + int32 + "; int64 = " + int64);
                        System.out.println("==================================================");
                    } else if (funCode == BaseFrame.FUNC_STRING){
                        String strData = frame.getRequest().getStringRequest().getStrData();
                        System.out.println("==================================================");
                        System.out.println("strData = " + strData);
                        System.out.println("==================================================");
                    }
                }

                //返回到客户端
                SocketServer.getInstance().sendDataToClient(data);
            }
        });
    }
}
