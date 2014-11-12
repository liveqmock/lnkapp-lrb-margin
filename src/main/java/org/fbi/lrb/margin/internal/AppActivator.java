package org.fbi.lrb.margin.internal;

import org.fbi.linking.processor.ProcessorManagerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class AppActivator implements BundleActivator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static BundleContext context;
    private ServerService serverService;

    public static BundleContext getBundleContext() {
        return context;
    }

    public void start(BundleContext context) {
        AppActivator.context = context;

        startServer();

        ProcessorFactory factory = new ProcessorFactory();
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("APPID", "LRBMGN");
        context.registerService(ProcessorManagerService.class.getName(), factory, properties);

        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Starting the Lrb-margin app bundle...." );
    }

    private void startServer() {
        this.serverService = new ServerService(getBundleContext());
        this.serverService.start();
    }

    public void stop(BundleContext context) throws Exception {
        this.serverService.stop();
        AppActivator.context = null;
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Stopping the Lrb-margin app bundle...");
    }

}
