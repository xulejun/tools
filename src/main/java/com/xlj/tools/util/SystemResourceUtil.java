package com.xlj.tools.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.xlj.tools.bean.SystemResourceInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author legend xu
 * @date 2023/2/7
 */
public class SystemResourceUtil {
    public static void main(String[] args) {
        // jvm 资源信息
        SystemResourceInfo monitor = new SystemResourceInfo();
        monitor.setHostName(SystemUtil.getHostInfo().getName());
        monitor.setIp(SystemUtil.getHostInfo().getAddress());

        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        monitor.setRunTimeMaxMemory(FileUtil.readableFileSize(runtimeInfo.getMaxMemory()));
        monitor.setRunTimeTotalMemory(FileUtil.readableFileSize(runtimeInfo.getTotalMemory()));
        monitor.setRunTimeFreeMemory(FileUtil.readableFileSize(runtimeInfo.getFreeMemory()));
        monitor.setRunTimeUsableMemory(FileUtil.readableFileSize(runtimeInfo.getUsableMemory()));

        OsInfo osInfo = SystemUtil.getOsInfo();
        monitor.setOsType(osInfo.getArch());
        monitor.setOsVersion(osInfo.getName());

        monitor.setJavaRuntimeVersion(SystemUtil.getJavaRuntimeInfo().getVersion());

        // 系统资源信息
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        monitor.setCpuUsedRate(decimalFormat.format((100 - cpuInfo.getFree()) / 100));

        GlobalMemory memory = OshiUtil.getMemory();
        monitor.setTotalMemory(FileUtil.readableFileSize(memory.getTotal()));
        long usedMemory = memory.getTotal() - memory.getAvailable();
        monitor.setUsedMemory(FileUtil.readableFileSize(usedMemory));
        monitor.setUsedMemoryRate(decimalFormat.format((double) usedMemory / (double) memory.getTotal()));

        List<HWDiskStore> diskStores = OshiUtil.getDiskStores();
        monitor.setDiskTotalSize(FileUtil.readableFileSize(diskStores.get(0).getSize()));

        System.out.println(monitor.toString());
    }
}
