/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gb.apm.collector.netty;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.collector.FutureListener;
import com.gb.apm.collector.ResponseMessage;
import com.gb.apm.model.APMModel;
import com.gb.apm.model.TAgentInfo;
import com.gb.apm.model.TApiMetaData;
import com.gb.apm.remoting.InvokeCallback;
import com.gb.apm.remoting.RemotingClient;
import com.gb.apm.remoting.exception.RemotingConnectException;
import com.gb.apm.remoting.exception.RemotingSendRequestException;
import com.gb.apm.remoting.exception.RemotingTimeoutException;
import com.gb.apm.remoting.exception.RemotingTooMuchRequestException;
import com.gb.apm.remoting.netty.NettyClientConfig;
import com.gb.apm.remoting.netty.NettyRemotingClient;
import com.gb.apm.remoting.protocol.RemotingCommand;

/**
 * @author emeroad
 * @author koo.taejin
 * @author netspider
 */
public class TcpEnhancedDataSender implements EnhancedDataSender {
	
	private final RemotingClient remotingClient;
	
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "DbLoggerClientScheduledThread");
        }
    });
	
	private final ReentrantLock lockHeartbeat = new ReentrantLock();
	
	public TcpEnhancedDataSender() {
		NettyClientConfig nettyClientConfig = new NettyClientConfig();//TODO ClientConfig to NettyClientConfig
		this.remotingClient = new NettyRemotingClient(nettyClientConfig, null);
	}
	
	public void start() {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(lockHeartbeat.tryLock()) {
					try {
//						HeartBeatData heartBeatData = generateHeartBeatData();
//						try {
//							clientApi.heartBeat(heartBeatData);
//						} catch (RemotingException | InterruptedException | HeartBeatException e) {
//							log.error("心跳发送失败", e);
//						}
					} finally {
						lockHeartbeat.unlock();
					}
				}else {
				}
			}
		}, 1000, 3000, TimeUnit.MILLISECONDS);
		remotingClient.start();
	}
	
	public void shutdown() {
		try {
			if(lockHeartbeat.tryLock(3000, TimeUnit.MILLISECONDS)) {
				try {
					this.scheduledExecutorService.shutdown();
					this.remotingClient.shutdown();
				} finally {
					lockHeartbeat.unlock();
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean send(APMModel data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	private RemotingCommand buildCmd(APMModel data) {
		try {
			RemotingCommand cmd = null;
			if(data instanceof TAgentInfo) {
				cmd  = RemotingCommand.createRequestCommand(0, null);
				cmd.setBody(JSON.toJSONBytes(data));
			}else if(data instanceof TApiMetaData) {
				cmd = RemotingCommand.createRequestCommand(1, null);
				cmd.setBody(JSON.toJSONBytes(data));
			}
			return cmd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean request(APMModel data) {
		RemotingCommand cmd = buildCmd(data);
		if(cmd == null) return false;
		try {
			remotingClient.invokeOneway("", cmd, 3000);
			return true;
		} catch (RemotingConnectException | RemotingTooMuchRequestException | RemotingTimeoutException
				| RemotingSendRequestException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public RemotingCommand request(APMModel data, int retry) {
		RemotingCommand cmd = buildCmd(data);
		if(cmd == null) return null;
		return null;
	}

//	@Override
//	public void request(APMModel data, InvokeCallback callback) {
//		RemotingCommand cmd = buildCmd(data);
//		if(cmd == null) return;
//		try {
//			remotingClient.invokeAsync("", cmd, 3000, callback);
//		} catch (RemotingConnectException | RemotingTooMuchRequestException | RemotingTimeoutException
//				| RemotingSendRequestException | InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
//		return;
//	}

	@Override
	public void requestAysn(APMModel data) {
		RemotingCommand cmd = buildCmd(data);
		if(cmd == null) return;
		try {
			remotingClient.invokeAsync("", cmd, 3000, null);
		} catch (RemotingConnectException | RemotingTooMuchRequestException | RemotingTimeoutException
				| RemotingSendRequestException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		return;
	}

	@Override
	public void request(APMModel data, FutureListener<ResponseMessage> listener) {
		// TODO Auto-generated method stub
		
	}


}
