package com.gb.apm.server.compoent;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.gb.apm.model.TAgentInfo;
import com.gb.apm.model.TApiMetaData;
import com.gb.apm.remoting.netty.NettyRequestProcessor;
import com.gb.apm.remoting.protocol.RemotingCommand;
import com.gb.apm.remoting.protocol.RequestCode;
import com.gb.apm.remoting.protocol.ResponseCode;
import com.gb.apm.server.Result;
import com.gb.apm.server.service.ApmMessageHandlerService;
import com.google.inject.Inject;

import io.netty.channel.ChannelHandlerContext;

public class ApmMessageProcessor implements NettyRequestProcessor {
	
	private final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	
	private final ApmMessageHandlerService handler;
	
	@Inject
	public ApmMessageProcessor(ApmMessageHandlerService handler) {
		this.handler = handler;
	}

	@Override
	public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
		final RemotingCommand response = RemotingCommand.createResponseCommand(ResponseCode.SUCCESS, "");
		switch (request.getCode()) {
		case RequestCode.AGENT_INFO:
			handleAgentInfo(request,response);
			break;
		case RequestCode.API_META_INFO:
			handleApiMetainfo(request,response);
			break;
		default:
			break;
		}
		return response;
	}

	@Override
	public boolean rejectRequest() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void handleAgentInfo(RemotingCommand request,RemotingCommand response) {
		TAgentInfo agentInfo = decode(request.getBody(),TAgentInfo.class);
		handler.handleAgentInfo(agentInfo);
	}
	
	private void handleApiMetainfo(RemotingCommand request,RemotingCommand response) {
		TApiMetaData apiMetaInfo = decode(request.getBody(),TApiMetaData.class);
		Result result = handler.cacheApi(apiMetaInfo);
		response.setBody(encode(result));
	}

	public static <T> T decode(final byte[] data, Class<T> classOfT) {
        final String json = new String(data, CHARSET_UTF8);
        return fromJson(json, classOfT);
    }
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }
	
	public static String toJson(final Object obj, boolean prettyFormat) {
        return JSON.toJSONString(obj, prettyFormat);
    }
	
	public static byte[] encode(final Object obj) {
        final String json = toJson(obj,false);
        if (json != null) {
            return json.getBytes(CHARSET_UTF8);
        }
        return null;
    }
}
