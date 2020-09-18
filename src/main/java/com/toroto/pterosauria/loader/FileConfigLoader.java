package com.toroto.pterosauria.loader;

import com.toroto.pterosauria.domain.db.ConfigDO;
import com.toroto.pterosauria.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
@Slf4j
public class FileConfigLoader extends AbstractConfigLoader {

    @Value("${config.file.list: []}")
    private List<String> fileList;

    @Override
    public ConfigDO load(String uri, String method) {
        String configString;
        ConfigDO config;
        byte[] buffer;
        FileInputStream inputStream = null;
        for (String fileName : fileList) {
            try {
                inputStream = new FileInputStream(fileName);
                buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
            } catch (IOException e) {
                log.error("读取文件失败：", e);
                continue;
            }
            configString = new String(buffer);
            config = JsonUtil.fromJson(configString, ConfigDO.class);
            if (null == config) {
                continue;
            }
            if (uri.equalsIgnoreCase(config.getUri()) && method.equalsIgnoreCase(config.getMethod())) {
                return config;
            }
        }
        return null;
    }
}
