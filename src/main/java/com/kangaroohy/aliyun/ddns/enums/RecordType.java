package com.kangaroohy.aliyun.ddns.enums;

import lombok.Getter;

/**
 * 类 RecordType 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/1 23:51
 */
@Getter
public enum RecordType {
    /**
     * 记录类型
     */
    A("A"),
    AAAA("AAAA");

    private final String type;

    RecordType(String type) {
        this.type = type;
    }
}
