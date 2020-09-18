CREATE TABLE `mock_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uri` varchar(255) NOT NULL COMMENT '请求uri',
  `method` varchar(8) NOT NULL COMMENT '请求方式',
  `type` tinyint(4) DEFAULT '0' COMMENT '响应类型：0同步，1异步',
  `sync_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_response` varchar(2000) DEFAULT NULL COMMENT '同步返回参数，长度可以自己调整',
  `async_call_path` varchar(255) DEFAULT NULL COMMENT '异步调用路径',
  `async_method` varchar(8) DEFAULT NULL COMMENT '异步接口请求方式',
  `delay_seconds` int(11) DEFAULT NULL COMMENT '延迟，秒',
  `response_content_type` varchar(64) NOT NULL DEFAULT '' COMMENT '返回响应中的 contentType',
  `async_http_header` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '异步接口header，json形式的字符串',
  PRIMARY KEY (`id`),
  KEY `idx_uri_method` (`uri`,`method`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='mock配置';

INSERT INTO `mock_config`(`id`, `uri`, `method`, `type`, `sync_response`, `async_response`, `async_call_path`, `async_method`, `delay_seconds`, `response_content_type`, `async_http_header`) VALUES (1, '/test', 'GET', 0, '{"method": "get", "helloWorld": {{query:test}}}', NULL, NULL, NULL, NULL, 'application/json;charset=UTF-8', '');
INSERT INTO `mock_config`(`id`, `uri`, `method`, `type`, `sync_response`, `async_response`, `async_call_path`, `async_method`, `delay_seconds`, `response_content_type`, `async_http_header`) VALUES (2, '/test', 'POST', 1, '{"method": "post", "helloWorld": {{body:helloWorld}}, "randomString": {{UUID}}, "randomNumber": {{RANDOM_NUMBER}}, "inner":{{body:testObj:objObj:innerData}}}', '{"response": "test", "testText":{{body:testText}}}', 'http://localhost:9099/test', 'GET', 10, 'application/json;charset=UTF-8', '{"Content-Type": "application/json;charset=UTF-8", "Authorization": "ttt"}');