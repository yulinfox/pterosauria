package com.toroto.pterosauria.loader;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
@Slf4j
public class FileConfigLoader extends AbstractConfigLoader {

    @Value("${config.file.root: \"config/\"}")
    private String configRoot;

    @Value("${config.file.path: \"\"}")
    private String configFilePath;

    private final Map<String, String> configMap = new ConcurrentHashMap<>();
    private final Set<String> fileSet = new CopyOnWriteArraySet<>();

    private synchronized void loadConfigMap() {
        if ("".equalsIgnoreCase(configFilePath)) {
            return;
        }
        configMap.clear();
        try {
            InputStream inputStream = new FileInputStream(configFilePath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String content = new String(buffer, Charset.forName("UTF-8"));
            configMap.putAll(JsonUtil.fromJson(content, Map.class));
        } catch (Exception e) {
            log.error("load config file failed: ", e);
        }
    }

    /**
     * 加载文件名
     */
    private synchronized void loadFileList() {
        if ("".equalsIgnoreCase(configRoot)
                || null == configRoot
                || !CollectionUtils.isEmpty(fileSet)) {
            return;
        }
        File root = new File(configRoot);
        addFileList(root);
    }

    private void addFileList(File root) {
        if (null == root) {
            return;
        }
        File[] files = root.listFiles();
        if (null == files) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                fileSet.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                addFileList(file);
            }
        }
    }

    @Override
    public ConfigDO load(String uri, String method) {
        // 优先从configFilePath中配置的文件路径获取文件名
        this.loadConfigMap();
        ConfigDO config = this.loadFromConfigMap(uri, method);
        if (null != config) {
            // 如果获取到配置，直接返回
            return config;
        }

        // 如果没有从给定的map中获取到数据，从fileList中获取
        loadFileList();
        String configString;
        byte[] buffer;
        FileInputStream inputStream;
        for (String fileName : fileSet) {
            try {
                inputStream = new FileInputStream(fileName);
                buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
            } catch (IOException e) {
                log.error("读取文件失败：", e);
                continue;
            }
            configString = new String(buffer);
            try {
                config = JsonUtil.fromJson(configString, ConfigDO.class);
            } catch (Exception e) {
                log.error("文件内容转换失败：", e);
                continue;
            }
            if (null == config) {
                continue;
            }
            if (uri.equalsIgnoreCase(config.getUri()) && method.equalsIgnoreCase(config.getMethod())) {
                return config;
            }
        }
        return null;
    }

    /**
     * 优先从配置文件中获取
     * @param uri
     * @param method
     * @return
     */
    private ConfigDO loadFromConfigMap(String uri, String method) {
        if (CollectionUtils.isEmpty(configMap)) {
            return null;
        }
        String fileName = configMap.get(String.format("%s-%s", uri, method.toUpperCase()));
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        byte[] buffer;
        try {
            InputStream inputStream = new FileInputStream(fileName);
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        } catch (IOException e) {
            log.error("读取文件失败：", e);
            return null;
        }
        String configString = new String(buffer);
        try {
            ConfigDO config = JsonUtil.fromJson(configString, ConfigDO.class);
            return config;
        } catch (Exception e) {
            log.error("文件内容转换失败：", e);
            return null;
        }
    }
}
