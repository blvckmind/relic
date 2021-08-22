package io.github.blvckmind.relic.model

import io.github.blvckmind.relic.config.ApplicationProperties
import io.github.blvckmind.relic.util.loggerFor
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import kotlin.Throws
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LocalhostInterceptor(val applicationProperties: ApplicationProperties) : HandlerInterceptor {

    private val log = loggerFor(LocalhostInterceptor::class.java)

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (applicationProperties.authorization.outsideAccess) {
            return true
        }

        var ipAddress = request.getHeader("X-FORWARDED-FOR")
        if (ipAddress == null) {
            ipAddress = request.remoteAddr
        }

        if (ipAddress != null && ipAddress.equals("0:0:0:0:0:0:0:1")){
            return true
        }

        log.warn("External IP '${ipAddress}' trying to connect")
        return false
    }
}