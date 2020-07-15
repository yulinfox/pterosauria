CREATE TABLE `mock_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uri` varchar(255) NOT NULL COMMENT '请求uri',
  `method` varchar(8) NOT NULL COMMENT '请求方式',
  `type` tinyint(4) DEFAULT 0 COMMENT '响应类型：0同步，1异步',
  `sync_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_call_path` varchar(255) COMMENT '异步调用路径',
  `async_method` VARCHAR(8) COMMENT '异步接口请求方式',
  `response_content_type` varchar(64) NOT NULL DEFAULT '' COMMENT '返回响应中的 contentType',
  `async_content_type` varchar(64) NOT NULL DEFAULT '' COMMENT '异步接口contentType',
  PRIMARY KEY (`id`),
	key `idx_uri_method`(`uri`, `method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='mock配置';