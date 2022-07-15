create schema personal collate utf8_general_ci;

-- file_info: table
CREATE TABLE `file_info`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL COMMENT '文件名',
    `path` varchar(255) DEFAULT NULL COMMENT '路径',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='文件信息';

-- product: table
CREATE TABLE `product`
(
    `id`    int(11) NOT NULL AUTO_INCREMENT,
    `name`  varchar(100)   DEFAULT NULL COMMENT '商品名称',
    `price` decimal(10, 0) DEFAULT NULL COMMENT '价格',
    `stock` varchar(255)   DEFAULT NULL COMMENT '秒杀随机码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- user: table
CREATE TABLE `user`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL COMMENT '用户名',
    `password` varchar(100) DEFAULT NULL COMMENT '密码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

