package com.example.apigatewayservice.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LoggingFilter : AbstractGatewayFilterFactory<LoggingFilter.Config>(Config::class.java) {
    private val log = LoggerFactory.getLogger(LoggingFilter::class.java)


    class Config(
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger: Boolean
    )

    override fun apply(config: Config?): GatewayFilter {
        // Custom Pre Filter
//        return GatewayFilter { exchange, chain ->
//            val request = exchange.request
//            val response = exchange.response
//
//            log.info("Global Filter baseMessage: {}", config!!.baseMessage)
//
//            if(config!!.preLogger) {
//                log.info("Global Filter Start: request id -> {}", request.id)
//            }
//
//            // Custom Post Filter
//            chain.filter(exchange).then(Mono.fromRunnable {
//                if(config.postLogger){
//                    log.info("Global Filter End: response code -> {}", response.statusCode)
//                }
//            })
//        }
        val filter: GatewayFilter = OrderedGatewayFilter({ exchange, chain ->
            val request = exchange.request
            val response = exchange.response

            log.info("Logging Filter baseMessage: {}", config!!.baseMessage)

            if (config!!.preLogger) {
                log.info("Logging PRE Filter Start: request id -> {}", request.id)
            }

            // Custom Post Filter
            chain.filter(exchange).then(Mono.fromRunnable {
                if (config.postLogger) {
                    log.info("Logging POST Filter End: response code -> {}", response.statusCode)
                }
            })
        }, Ordered.HIGHEST_PRECEDENCE)
        return filter
    }
}