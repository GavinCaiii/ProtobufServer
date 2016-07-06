package com.caitou.data;

import com.caitou.protocol.Protocol;
import com.caitou.protocol.RequestProto;
import com.caitou.protocol.ResponseProto;

/**
 * Created by Mr.Caiii on 2016-07-04.
 */
public class StringResponseData extends BaseFrame{

    public Request request;

    public StringResponseData() {
        this.request = new Request();
    }

    public static StringResponseData create() {
        StringResponseData stringResponseData = new StringResponseData();
        stringResponseData.initHead(CTRL_SERVER_TO_CLIENT, FUNC_STRING);
        stringResponseData.request.rStr = "I'm from server";

        return stringResponseData;
    }

    public static StringResponseData initData(String data) {
        StringResponseData stringResponseData = new StringResponseData();
        stringResponseData.initHead(CTRL_SERVER_TO_CLIENT, FUNC_STRING);
        stringResponseData.request.rStr = "from server : " + data;

        return stringResponseData;
    }

    @Override
    public Protocol.Frame toFrame() {
        Protocol.Frame.Builder frameBuilder = Protocol.Frame.newBuilder();
        RequestProto.Request.Builder requestBuilder = RequestProto.Request.newBuilder();
        RequestProto.StringRequest.Builder strBuilder = RequestProto.StringRequest.newBuilder();

        strBuilder.setStrData(this.request.rStr);

        requestBuilder.setStringRequest(strBuilder);

        frameBuilder.setHeader(getFrameHeader());
        frameBuilder.setRequest(requestBuilder);

        return frameBuilder.build();
    }

    public class Request {
        public String rStr;
    }
}
