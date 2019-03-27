package com.gb.apm.collector;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.model.APMModel;

public abstract class QueueDataSender implements DataSender {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static enum InvokeStackEvent{
		SUCCESS("success"),
		PUSH("push"),
		POP("pop"),
		EXCEPTION("exception"),
		ERROR("error"),
		FINISH("finish"),
		AGENTINFO("agentinfo"),
		;
		String type;
		InvokeStackEvent(String type) {
			this.type = type;
		}
		public String getType() {
			return type;
		}
	}
	
	private BlockingQueue<APMModel> queue = new LinkedBlockingQueue<APMModel>();
	private Thread worker = null;
	private AtomicBoolean workerStatus = new AtomicBoolean(false);
	
	private final InvokeStackEvent interest;
	
	public abstract boolean deal(APMModel frame);//子类实现具体处理细节
	
	public QueueDataSender(InvokeStackEvent interest) {
		this.interest = interest;
		if (this.worker == null) {
			this.worker = new Thread(new Runnable() {
				public void run() {
					APMModel frame;
					try {
						logger.info("asm队列处理线程开始");
						workerStatus.compareAndSet(false, true);
						while((frame = queue.take()) != null) {
							try {
								deal(frame);
							} catch (Throwable e) {
								logger.error("asm队列处理任务处理失败{}:{}",e);
							}
						}
					} catch (Exception e) {
						logger.error("asm队列处理线程异常结束!!!",e);
						workerStatus.compareAndSet(true, false);
						e.printStackTrace();
					}
				}
			},"InvokeStackEvent-"+interest.getType()+"-Handler");
			this.worker.start();
		}
	}

	@Override
	public boolean send(APMModel data) {
		if(workerStatus.get()) {
			return queue.offer(data);
		}
		return false;
	}

	@Override
	public void stop() {
		workerStatus.compareAndSet(true, false);
		worker.interrupt();
		queue.clear();
		queue = null;
		
	}
}
