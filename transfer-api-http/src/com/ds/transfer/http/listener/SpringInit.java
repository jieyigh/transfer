package com.ds.transfer.http.listener;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.http.constants.ApplicationContstants;
import com.ds.transfer.http.controller.TransferControllerNew;
import com.ds.transfer.http.service.SupportTransferService;

/**
 * spring 初始化需要准备些数据
 * 
 * @author jackson
 *
 */
@Component
public class SpringInit implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			TransferControllerNew controller = event.getApplicationContext().getBean(TransferControllerNew.class);
			Field[] declaredFields = controller.getClass().getDeclaredFields();
			Map<String, TransferService<?>> transferServiceMap = ApplicationContstants.TRANSFER_SERVICE_MAP;//转账服务
			Map<String, SupportTransferService<?>> supportServiceMap = ApplicationContstants.SUPPORT_SERVICE_MAP;//支撑转账服务
			for (Field field : declaredFields) {
				if (field.getName().endsWith("TransferService")) {//转账service层
					String prefix = field.getName().substring(0, field.getName().indexOf("TransferService"));
					transferServiceMap.put(prefix, (TransferService<?>) event.getApplicationContext().getBean(prefix + "TransferServiceImpl"));//注解xxxTransferServiceImpl
					supportServiceMap.put(prefix, (SupportTransferService<?>) event.getApplicationContext().getBean(prefix + "TransferServiceImpl"));
				}
			}
		}
	}

}
