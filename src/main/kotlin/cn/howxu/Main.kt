package cn.howxu

import cn.howxu.http.HttpResponse
import org.apache.logging.log4j.LogManager
import java.net.ServerSocket

fun main() {
    //println("Hello Kotlin")
    val logger = LogManager.getLogger()
    logger.info("Kotlin HttpServer Start")
    HttpServer(8080).launch()
}

/**@author HowXu
 *
 * 闺蜜你要的HttpServer来了
 *
 */
class HttpServer(port: Int) {
    private val server = ServerSocket(port)

    fun launch() {
        val router = Router()

        // lambda表达式作为handler参数 这个lambda表达式可以移动到外面
        router.registe("/", { _ ->
            // 直接return会从外层函数launch出去
            return@registe HttpResponse(statusCode = 200, statusText = "OK")
        })

        router.registe("/hello", { _ ->
            return@registe HttpResponse(
                statusCode = 200,
                statusText = "OK",
                headers = mapOf("Content-Type" to "text/html;charset=UTF-8"),
                responseText = "<h1>Hello World</h1>"
            )
        })

        // 在这里开始阻塞监听
        router.handle(server)
    }
}