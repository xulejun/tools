package com.xlj.tools.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author legend xu
 * @date 2023/2/6
 */
@Data
public class SystemResourceInfo {
    private String hostName;
    private String ip;

    private String runTimeMaxMemory;
    private String runTimeTotalMemory;
    private String runTimeFreeMemory;
    private String runTimeUsableMemory;

    private String osType;
    private String osVersion;

    private String javaRuntimeVersion;

    private String cpuUsedRate;

    private String totalMemory;
    private String usedMemory;
    private String usedMemoryRate;

    private String diskTotalSize;
}
