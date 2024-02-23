import com.faendir.awscdkkt.generated.app
import com.faendir.awscdkkt.generated.services.lambda.buildFunction
import com.faendir.awscdkkt.generated.stack
import software.amazon.awscdk.services.lambda.Runtime
import software.amazon.awscdk.services.lambda.SnapStartConf
import software.amazon.awscdk.services.lambda.Code

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.faendir.awscdkkt:dsl:2.129.0-1.0.1")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
}

val buildZip by tasks.registering(Zip::class) {
    group = "distribution"
    from(tasks.compileKotlin)
    from(tasks.processResources)
    into("lib") { from(configurations.runtimeClasspath) }
}

val synth by tasks.registering {
    dependsOn(buildZip)
    doLast {
        app {
            stack("pjcc-test-stack") {
                buildFunction("pjcc-test-s3events-handler") {
                    runtime(Runtime.JAVA_21)
                    environment(mapOf("JAVA_TOOL_OPTIONS" to "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"))
                    code(Code.fromAsset(buildZip.get().outputs.files.singleFile.absolutePath))
                    handler("de.codecentric.pjmeisch.S3EventHandler::handleEvent")
                    snapStart(SnapStartConf.ON_PUBLISHED_VERSIONS)
                    memorySize(2048)
                }
            }
        }.synth()
    }
}
