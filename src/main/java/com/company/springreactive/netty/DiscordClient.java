package com.company.springreactive.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiscordClient {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {

                        @Override
                        protected void initChannel(@NonNull Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DiscordClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("localhost", 8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    static class DiscordClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws IOException {

            String sendMessage = "";

            do {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                sendMessage = br.readLine();

                ByteBuf messageBuffer = Unpooled.buffer();
                messageBuffer.writeBytes(sendMessage.getBytes());

                String builder = "전송한 문자열: [" + sendMessage + "]";

                System.out.println(builder);
                ctx.writeAndFlush(messageBuffer);
            } while (!sendMessage.equals("exit"));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

    }
}
