package com.abc.chat4j.im.netty;

public interface ImServer {

    boolean isReady();

    void start();

    void stop();
}
