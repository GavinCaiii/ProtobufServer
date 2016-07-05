package com.caitou.data;

import com.caitou.protocol.Protocol;

/**
 * Created by Mr.Caiii on 2016-07-04.
 */
public abstract class BaseFrame {
    public static final int CTRL_CLIENT_TO_SERVER = 0x01;
    public static final int CTRL_SERVER_TO_CLIENT = 0x02;

    public static final int FUNC_INT = 0x01;
    public static final int FUNC_STRING = 0x02;

    public int head;
    public int controlCode;
    public int functionCode;

    public void initHead(int controlCode, int functionCode){
        this.head = 0x55;
        this.controlCode = controlCode;
        this.functionCode = functionCode;
    }

    protected void paresFromHeader(Protocol.Header header) {
        this.controlCode = header.getControlCode();
        this.functionCode = header.getFunctionCode();
    }

    protected Protocol.Header getFrameHeader() {
        Protocol.Header.Builder headerBuilder = Protocol.Header.newBuilder();
        headerBuilder.setHead(this.head)
                .setControlCode(this.controlCode)
                .setFunctionCode(this.functionCode);
        return headerBuilder.build();
    }

    public abstract Protocol.Frame toFrame();
}
