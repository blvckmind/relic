package io.github.blvckmind.relic.model

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import kotlin.Throws
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LocalhostInterceptor : HandlerInterceptor {


    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

//        var ipAddress = request.getHeader("X-FORWARDED-FOR")
//        if (ipAddress == null) {
//            ipAddress = request.remoteAddr
//        }
//
//        if (ipAddress != null && ipAddress.equals("0:0:0:0:0:0:0:1")){
//            return true
//        }
//
//        throw RuntimeException("External IP:'$ipAddress'")
//
        return true
    }
}