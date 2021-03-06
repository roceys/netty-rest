package geidsvig.netty.server

import java.net.InetSocketAddress
import java.util.concurrent.Executors
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory

trait RestServerRequirements {
  val logger: akka.event.LoggingAdapter
  val port: Int
  val timeout: Long
  val pipelineFactory: RestServerPipelineFactory
}

class RestServer {
  self: RestServerRequirements =>

  def run() {
    val bootstrap = new ServerBootstrap(
      new NioServerSocketChannelFactory(
        Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool()))

    bootstrap.setOption("child.keepAlive", false)
    bootstrap.setOption("child.tcpNoDelay", true)
    bootstrap.setOption("reuseAddress", true)
    bootstrap.setOption("connectTimeoutMillis", timeout)

    bootstrap.setPipelineFactory(pipelineFactory)

    bootstrap.bind(new InetSocketAddress(port))

    logger info ("RestServer started on port " + port)
  }
}
