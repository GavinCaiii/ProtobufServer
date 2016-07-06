package com.caitou.data;

import com.caitou.protocol.Protocol;
import com.caitou.protocol.RequestProto;

/**
 * Created by Mr.Caiii on 2016-07-04.
 */
public class IntResponseData extends BaseFrame {
    public Request request;
    public Response response;

    public IntResponseData() {
        this.request = new Request();
        this.response = new Response();
    }

    public static IntResponseData create(){
        IntResponseData intResponseData = new IntResponseData();
        intResponseData.initHead(CTRL_SERVER_TO_CLIENT, FUNC_INT);
        intResponseData.response.rInt32 = 456;
        intResponseData.response.rInt64 = 123456;

        return intResponseData;
    }

    @Override
    public Protocol.Frame toFrame() {
        Protocol.Frame.Builder frameBuilder = Protocol.Frame.newBuilder();
        RequestProto.Request.Builder reqBuilder = RequestProto.Request.newBuilder();
        RequestProto.IntRequest.Builder intReqBuilder = RequestProto.IntRequest.newBuilder();

        intReqBuilder.setInt32Data(this.response.rInt32);
        intReqBuilder.setInt64Data(this.response.rInt64);

        reqBuilder.setIntRequest(intReqBuilder);
        frameBuilder.setHeader(getFrameHeader());
        frameBuilder.setRequest(reqBuilder);

        return frameBuilder.build();
    }

    public static IntResponseData parseFrom(Protocol.Frame frame){
        IntResponseData intResponseData = new IntResponseData();
        intResponseData.paresFromHeader(frame.getHeader());

        intResponseData.response.rInt32 = frame.getResponse().getIntResponse().getInt32Data();
        intResponseData.response.rInt64 = frame.getResponse().getIntResponse().getInt64Data();

        return intResponseData;
    }

    public class Request {
        public int int32;
        public long int64;
    }

    public class Response {
        public int rInt32;
        public long rInt64;
    }
}
