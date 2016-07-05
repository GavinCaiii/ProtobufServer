package com.caitou.data;

import com.caitou.protocol.Protocol;
import com.caitou.protocol.RequestProto;
import com.caitou.protocol.ResponseProto;

/**
 * Created by Mr.Caiii on 2016-07-04.
 */
public class IntRequestData extends BaseFrame {
    public Request request;
    public Response response;

    public IntRequestData() {
        this.request = new Request();
        this.response = new Response();
    }

    public static IntRequestData create(){
        IntRequestData intRequestData = new IntRequestData();
        intRequestData.initHead(CTRL_SERVER_TO_CLIENT, FUNC_INT);
        intRequestData.request.int32 = 456;
        intRequestData.request.int64 = 123456;

        return intRequestData;
    }

    @Override
    public Protocol.Frame toFrame() {
        Protocol.Frame.Builder frameBuilder = Protocol.Frame.newBuilder();
        RequestProto.Request.Builder reqBuilder = RequestProto.Request.newBuilder();
        RequestProto.IntRequest.Builder intReqBuilder = RequestProto.IntRequest.newBuilder();

        intReqBuilder.setInt32Data(this.request.int32);
        intReqBuilder.setInt64Data(this.request.int64);

        reqBuilder.setIntRequest(intReqBuilder);
        frameBuilder.setHeader(getFrameHeader());
        frameBuilder.setRequest(reqBuilder);

        return frameBuilder.build();
    }

    public static IntRequestData parseFrom(Protocol.Frame frame){
        IntRequestData intRequestData = new IntRequestData();
        intRequestData.paresFromHeader(frame.getHeader());

        intRequestData.response.rInt32 = frame.getResponse().getIntResponse().getInt32Data();
        intRequestData.response.rInt64 = frame.getResponse().getIntResponse().getInt64Data();

        return intRequestData;
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
