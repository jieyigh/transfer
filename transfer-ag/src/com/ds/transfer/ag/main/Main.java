package com.ds.transfer.ag.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.container.Container;

public class Main {

	public static final String CONTAINER_KEY = "dubbo.container";

	public static final String SHUTDOWN_HOOK_KEY = "dubbo.shutdown.hook";

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final ExtensionLoader<Container> loader = ExtensionLoader.getExtensionLoader(Container.class);

	private static volatile boolean running = true;

	public static void main(String[] args) {
		try {
			if (args == null || args.length == 0) {
				String config = ConfigUtils.getProperty(CONTAINER_KEY, loader.getDefaultExtensionName());
				args = Constants.COMMA_SPLIT_PATTERN.split(config);
			}

			final List<Container> containers = new ArrayList<Container>();
			for (int i = 0; i < args.length; i++) {
				containers.add(loader.getExtension(args[i]));
			}
			logger.info("Use container type(" + Arrays.toString(args) + ") to run dubbo serivce.");

			if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						for (Container container : containers) {
							try {
								container.stop();
								logger.info("Dubbo " + container.getClass().getSimpleName() + " stopped!");
							} catch (Throwable t) {
								logger.error(t.getMessage(), t);
							}
							synchronized (Main.class) {
								running = false;
								Main.class.notify();
							}
						}
					}
				});
			}

			for (Container container : containers) {
				loadSomething();
				logger.info("Dubbo " + container.getClass().getSimpleName() + " started!");
			}
			logger.info((new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Dubbo service server started!"));
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		synchronized (Main.class) {
			while (running) {
				try {
					Main.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}

	private static void loadSomething() {
		PropertyConfigurator.configure("resource" + File.separator + "log4j.properties");
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("resource" + File.separator + "applicationContext.xml");
		context.start();
	}

}
