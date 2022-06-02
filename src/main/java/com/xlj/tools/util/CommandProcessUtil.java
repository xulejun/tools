package com.xlj.tools.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 不同环境下执行命令
 *
 * @author legend xu
 * @date 2022/6/1
 */
@Slf4j
public class CommandProcessUtil {
    /**
     * linux 系统下执行 shell 命令
     */
    public static String linuxExecute(List<String> commands) throws Exception {
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec("/bin/bash");
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String command : commands) {
                out.println(command);
            }
            // 这个命令必须执行，否则in流不结束
            out.println("exit");
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            // 资源关闭
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();

            return sb.toString();
        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        List<String> commands = new ArrayList<>();
        commands.add("cd /usr/local/redis/bin/");
        commands.add("./redis-cli -h 192.168.124.28 -p 6379 --bigkeys");
        String exec = linuxExecute(commands);
        log.info("shell 执行返回内容为：{}", exec);
    }
}
