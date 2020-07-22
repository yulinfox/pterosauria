CREATE TABLE `mock_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uri` varchar(255) NOT NULL COMMENT '请求uri',
  `method` varchar(8) NOT NULL COMMENT '请求方式',
  `type` tinyint(4) DEFAULT 0 COMMENT '响应类型：0同步，1异步',
  `sync_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_call_path` varchar(255) COMMENT '异步调用路径',
  `async_method` VARCHAR(8) COMMENT '异步接口请求方式',
  `delay_seconds` int (11) COMMENT "延迟，秒",
  `response_content_type` varchar(64) NOT NULL DEFAULT '' COMMENT '返回响应中的 contentType',
  `async_content_type` varchar(64) NOT NULL DEFAULT '' COMMENT '异步接口contentType',
  PRIMARY KEY (`id`),
	key `idx_uri_method`(`uri`, `method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='mock配置';

INSERT INTO `test`.`mock_config`(`id`, `uri`, `method`, `type`, `sync_response`, `async_response`, `async_call_path`, `async_method`, `delay_seconds`, `response_content_type`, `async_content_type`) VALUES (1, '/test', 'GET', 0, '{\"method\": \"get\", \"helloWorld\": {{query:test}}}', NULL, NULL, NULL, NULL, 'application/json;charset=UTF-8', '');
INSERT INTO `test`.`mock_config`(`id`, `uri`, `method`, `type`, `sync_response`, `async_response`, `async_call_path`, `async_method`, `delay_seconds`, `response_content_type`, `async_content_type`) VALUES (2, '/test', 'POST', 1, '{\"method\": \"post\", \"helloWorld\": {{body:helloWorld}}}', '{\"response\": \"hello world\"}', 'http://localhost:9099/test?test=ttt', 'GET', 10, 'application/json;charset=UTF-8', 'application/json;charset=UTF-8');
