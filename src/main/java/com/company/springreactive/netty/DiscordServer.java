package com.company.springreactive.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 서버가 클라이언트로부터 데이터를 8888 포트로 수신하는 간단한 기능을 수행하는 예제
 * 다시 말해 데이터만 받을 뿐 클라이언트 측에 어떠한 데이터도 보내지 않는다.
 */
@Slf4j
public class DiscordServer {

    /**
     * 클라이언트로부터 값을 읽어들이는 서버 (핸들러 지정 필수)
     * 다시 말해 서버의 8888번 포트에 클라이언트 접속을 허용하고 (inbound 허용)
     * 클라이언트로부터 받은 데이터를 DiscardServerHandler 클래스가 처리하도록 지정했다.
     */
    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap(); // 부트스트랩
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(@NonNull SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline(); // 채널 파이프라인
                            p.addLast(new DiscardServerHandler()); // 핸들러 지정
                        }
                    });

            // 부트스트랩 클래스의 bind 메서드로 접속할 포트를 지정한다.
            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    /**
     * 클라이언트로부터 받아들인 입력 데이터를 처리하는 핸들러.
     * Netty가 제공하는 SimpleChannelInboundHandler를 상속받고 메서드 재정의를 통해 사용한다.
     */
    static class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            // 내용이 비어있으면, 아무것도 클라이언트측에 보내지 않는다.

            String readMessage = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);

            log.info("{}", "수신 문자열 [" + readMessage + ']');
            ctx.write("서버로부터 온 메시지: " + msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }
    }
}
