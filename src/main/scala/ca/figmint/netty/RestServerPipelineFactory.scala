package ca.figmint.netty

import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.Channels
import org.jboss.netty.handler.codec.http.HttpChunkAggregator
import org.jboss.netty.handler.codec.http.HttpRequestDecoder
import org.jboss.netty.handler.codec.http.HttpResponseEncoder

trait RestServerPipelineFactorRequirements {
	val logger: akka.event.LoggingAdapter
	val routeHandler: RestServerRouteHandler
	val chunkSize: Int
}

abstract class RestServerPipelineFactory extends ChannelPipelineFactory {
	self: RestServerPipelineFactorRequirements =>
	
	def getPipeline() = {
		val instantiationTime = System.currentTimeMillis()
		
		val pipeline = Channels.pipeline()
		
		pipeline.addLast("decoder", new HttpRequestDecoder)
		pipeline.addLast("aggregator", new HttpChunkAggregator(chunkSize))
		pipeline.addLast("encoder", new HttpResponseEncoder)
		pipeline.addLast("handler", routeHandler)
		
		logger info "Pipeline created"
		
		pipeline
	}
}
