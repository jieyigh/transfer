package com.ds.transfer.scan.listener;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.scan.constants.ApplicationConstants;
import com.ds.transfer.scan.job.ScanRecordService;

@Component
public class SpringInit implements ApplicationListener<ContextRefreshedEvent> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			ScanRecordService controller = (ScanRecordService) event.getApplicationContext().getBean(ScanRecordService.class);
			Field[] declaredFields = controller.getClass().getDeclaredFields();
			Map<String, TransferService<?>> transferServiceMap = ApplicationConstants.transerServiceMap;
			StringBuilder prefixBuffer = new StringBuilder();
			for (Field field : declaredFields) {
				System.out.println(field.getName());
				if (field.getName().endsWith("TransferService")) {//转账service层
					String prefix = field.getName().substring(0, field.getName().indexOf("TransferService"));
					prefixBuffer.append(prefix).append(",");
					transferServiceMap.put(prefix, (TransferService<?>) event.getApplicationContext().getBean(prefix + "TransferService"));//注解xxxTransferServiceImpl
				}
			}
			logger.info("transferMap 存放的前缀 = {}", prefixBuffer.toString());
		}
	}
}
