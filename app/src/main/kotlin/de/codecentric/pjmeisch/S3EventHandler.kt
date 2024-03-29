package de.codecentric.pjmeisch

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.http4k.format.Jackson
import org.http4k.serverless.AwsLambdaEventFunction
import org.http4k.serverless.FnHandler
import org.http4k.serverless.FnLoader

val json = Jackson.apply {
    mapper.registerModule(JodaModule())
}

var environment = mutableMapOf<String, String>()

fun eventFnHandler() =
    FnHandler { s3Event: S3Event, context: Context ->
        context.logger.log("env:")
        context.logger.log(json.asFormatString(environment))
        context.logger.log("s3event:")
        context.logger.log(json.asFormatString(s3Event))
    }

fun eventFnLoader() =
    FnLoader { env: Map<String, String> ->
        environment += env
        eventFnHandler()
    }

class S3EventHandler : AwsLambdaEventFunction(eventFnLoader())
