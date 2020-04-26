# HTTP4S Request Body Error

Perform `sbt run` several times, and eventually you will receive this error:

The code causing this seems to be [`DemoClient:39-44`](src/main/scala/example/DemoClient.scala#L39-L44).

```
[INFO] org.http4s.blaze.channel.ServerChannel - Closing NIO1 channel /127.0.0.1:9010
io.netty.handler.codec.EncoderException: java.lang.IllegalStateException: unexpected message type: DefaultHttpRequest, state: 1
	at io.netty.handler.codec.MessageToMessageEncoder.write(MessageToMessageEncoder.java:107)
	at io.netty.channel.CombinedChannelDuplexHandler.write(CombinedChannelDuplexHandler.java:346)
	at io.netty.channel.AbstractChannelHandlerContext.invokeWrite0(AbstractChannelHandlerContext.java:715)
	at io.netty.channel.AbstractChannelHandlerContext.invokeWrite(AbstractChannelHandlerContext.java:707)
	at io.netty.channel.AbstractChannelHandlerContext.write(AbstractChannelHandlerContext.java:790)
	at io.netty.channel.AbstractChannelHandlerContext.write(AbstractChannelHandlerContext.java:700)
	at io.netty.handler.stream.ChunkedWriteHandler.doFlush(ChunkedWriteHandler.java:332)
	at io.netty.handler.stream.ChunkedWriteHandler.flush(ChunkedWriteHandler.java:133)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:748)
	at io.netty.channel.AbstractChannelHandlerContext.invokeWriteAndFlush(AbstractChannelHandlerContext.java:763)
	at io.netty.channel.AbstractChannelHandlerContext$WriteTask.run(AbstractChannelHandlerContext.java:1089)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at java.base/java.lang.Thread.run(Thread.java:832)
Caused by: java.lang.IllegalStateException: unexpected message type: DefaultHttpRequest, state: 1
	at io.netty.handler.codec.http.HttpObjectEncoder.encode(HttpObjectEncoder.java:86)
	at io.netty.handler.codec.http.HttpClientCodec$Encoder.encode(HttpClientCodec.java:167)
	at io.netty.handler.codec.MessageToMessageEncoder.write(MessageToMessageEncoder.java:89)
	... 16 more
```

**Interestingly** if you use a different server (i.e. not Blaze) the error doesn't occur.
 
Try changing the [`DemoClient:21`](src/main/scala/example/DemoClient.scala#L21) to use `val url = "http://localhost:8000/"` instead, and then run this to create a simple Python HTTP server:

```
python -m SimpleHTTPServer
```

The error goes away!