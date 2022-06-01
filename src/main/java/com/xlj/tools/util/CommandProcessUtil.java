package com.xlj.tools.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 不同环境下执行命令
 *
 * @author legend xu
 * @date 2022/6/1
 */
public class CommandProcessUtil {
    /**
     * linux 系统下执行 shell 命令
     *
     * @param cmd
     * @return
     */
    public static Object exec(String cmd) {
        try {
            // 多个命令组合
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            // 资源需要关闭
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
