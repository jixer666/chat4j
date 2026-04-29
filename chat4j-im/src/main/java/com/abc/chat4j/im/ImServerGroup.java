package com.abc.chat4j.im;

import com.abc.chat4j.im.netty.ImServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ImServerGroup implements CommandLineRunner {

    private final List<ImServer> imServers;

    @Override
    public void run(String... args) {
        // 启动服务
        for (ImServer imServer : imServers) {
            if (imServer.isReady()) {
                continue;
            }
            imServer.start();
        }
    }

    @PreDestroy
    public void destroy() {
        // 停止服务
        for (ImServer imServer : imServers) {
            imServer.stop();
        }
    }
}
