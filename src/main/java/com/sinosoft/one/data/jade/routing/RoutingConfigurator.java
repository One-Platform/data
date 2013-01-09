/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午04:36:01
 */

package com.sinosoft.one.data.jade.routing;

import com.sinosoft.one.data.jade.routing.router.DirectRouter;
import com.sinosoft.one.data.jade.routing.router.HashRouter;
import com.sinosoft.one.data.jade.routing.router.HexHashRouter;
import com.sinosoft.one.data.jade.routing.router.RangeRouter;
import com.sinosoft.one.data.jade.routing.router.RoundRouter;
import com.sinosoft.one.data.jade.routing.router.XmHashRouter;
import com.sinosoft.one.data.jade.routing.router.XmStringHashRouter;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RoutingConfigurator {
    static Log logger = LogFactory.getLog(RoutingConfigurator.class);

    protected ConcurrentHashMap<String, RoutingDescriptor> map = new ConcurrentHashMap<String, RoutingDescriptor>();

    private List<String> partitions;

    // 加锁保护配置信息
    protected ReadWriteLock rwLock = new ReentrantReadWriteLock();

    boolean inited = false;

    // 路由的名称
    public static final String DIRECT = "direct";

    public static final String ROUND = "round";

    public static final String RANGE = "range";

    public static final String HASH = "hash";

    public static final String XM_HASH = "xm-hash";

    public static final String XM_STRING_HASH = "xm-str-hash";

    public static final String HEX_HASH = "hex-hash";

    public RoutingDescriptor getDescriptor(String name) {
        if (!inited) {
            initPartitions();
        }

        String keyword = name;

        // 加锁保护配置信息的完整性
        Lock lock = rwLock.readLock();

        try {
            lock.lock();

            RoutingDescriptor descriptor = map.get(keyword);

            // if (descriptor == null) {
            //
            // descriptor = map.get(catalog); // 获取全局设置
            // }

            return descriptor;

        } finally {

            lock.unlock();
        }
    }

    private void initPartitions() {
        List<String> partitions = getPartitions();
        for (int i = 0; i < partitions.size(); i++) {
            String[] conf = partitions.get(i).split(":");
            map.put(conf[1], new RoutingDescriptorImpl().setPartitionRouter(createRouter(conf)));
        }
        inited = true;
    }

    public void setPartitions(List<String> partitions) {
        this.partitions = partitions;
    }

    public List<String> getPartitions() {
        return partitions;
    }

    private Router createRouter(String[] conf) {
        if (HASH.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(HASH) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new HashRouter(column, pattern, partitions);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).setPartition(conf[4]).createRouter();
        } else if (RANGE.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(RANGE) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new RangeRouter(column, pattern);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).createRouter();
        } else if (DIRECT.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(DIRECT) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new DirectRouter(column, pattern);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).createRouter();
        } else if (ROUND.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(ROUND) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new RoundRouter(pattern, partitions);
                }
            };
            return factory.setPattern(conf[3]).setPartition(conf[4]).createRouter();
        } else if (HEX_HASH.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(HEX_HASH) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new HexHashRouter(column, pattern, partitions);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).setPartition(conf[4]).createRouter();
        } else if (XM_HASH.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(XM_HASH) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new XmHashRouter(column, pattern, partitions);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).setPartition(conf[4]).createRouter();
        } else if (XM_STRING_HASH.equalsIgnoreCase(conf[0])) {
            RouterFactory factory = new RouterFactory(XM_STRING_HASH) {

                public Router onCreateRouter(String column, String pattern, int partitions) {
                    return new XmStringHashRouter(column, pattern, partitions);
                }
            };
            return factory.setColumn(conf[2]).setPattern(conf[3]).setPartition(conf[4]).createRouter();
        }
        return null;
    }
 
    private static abstract class RouterFactory {
        private String name;
        private Map<String, String> values = new HashMap<String, String>();
        
        public static final String KEY_COLUMN = "by-column";
        public static final String KEY_PATTERN = "target-pattern";
        public static final String KEY_PARTITION = "partitions";
        
        public RouterFactory(String name) {
            this.name = name;
        }
        
        public RouterFactory add(String key, String value) {
            values.put(key, value);
            return this;
        }
        
        public RouterFactory setColumn(String column) {
            return add(KEY_COLUMN, column);
        }
        
        public RouterFactory setPattern(String pattern) {
            return add(KEY_PATTERN, pattern);
        }
        
        public RouterFactory setPartition(String partition) {
            return add(KEY_PARTITION, partition);
        }
        
        public Router createRouter() {
            for(Entry<String, String> entry : this.values.entrySet()) {
                if (entry.getValue() == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format("Router '%s' must have '%s' property.", name, entry.getKey()));
                    }
                    return null;
                }
            }

            String column = values.get(KEY_COLUMN);
            String pattern = values.get(KEY_PATTERN);
            String partition = values.get(KEY_PARTITION);
            int count = 0;
            if (partition != null) {
                try {
                    count = NumberUtils.toInt(partition.trim());
                } catch (NumberFormatException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format("Router '%s' property '%s' must be number.", name, KEY_PARTITION));
                    }
                    return null;
                }
            }

            // 输出日志
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Creating router '%s' [ %s ]", name, values));
            }

            return onCreateRouter(column, pattern, count);
        }
        
        public abstract Router onCreateRouter(String column, String pattern, int partitions);
    }
}
