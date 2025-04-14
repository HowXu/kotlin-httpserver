package cn.howxu

import cn.howxu.http.Category
import cn.howxu.http.HttpRequest
import cn.howxu.http.HttpResponse
import java.net.ServerSocket
import kotlin.random.Random

// 处理Http请求 返回Response
typealias Handler = (HttpRequest) -> HttpResponse

/**@author HowXu
 * 路由数据
 */
data class Route(
    val category: Category,
    val route_path: String,
    val handler: Handler
)

/**@author HowXu
 *
 * 路由处理
 *
 */
class Router {
    // routes不可变但是可以改写内容 kotlin神力展现
    private val routes = mutableListOf<Route>()

    // 注册路由
    fun registe(path: String, handler: Handler) {
        routes.add(Route(Category.GET, path, handler))
    }

    // 处理服务器套接字输入的参数
    fun handle(socket: ServerSocket) {
        while (true){
            val client = socket.accept()
            val reader = client.getInputStream().bufferedReader()
            val writer = client.getOutputStream().bufferedWriter()
            val httpRequest = HttpRequest.parse(reader)
            /** 这里获得了传入套接字的请求 从路由中返回一个合适的位置
             * findLast 一直寻找到最后
             * 只有前面不为null 才会执行后面的let
             * 这个hanlder.invoke返回的值来自内部实现 这里应该理解为函数调用
             */
            routes.findLast { it.route_path == httpRequest.route_path && it.category == httpRequest.category }?.let {
                writer.write(it.handler.invoke(httpRequest).toString())
                writer.flush()
            } ?: let {
                // 为空就返回一个默认值
                writer.write(
                    HttpResponse(
                        statusCode = 404,
                        statusText = "Not Found",
                        headers = mapOf("Content-Type" to "text/html;charset=UTF-8"),
                        responseText = "<h1>${getJokeNotFound()}</h1>"
                    ).toString()
                )
                writer.flush()
            }

            // 结束这次套接字连接
            client.close()
        }
    }

    private fun getJokeNotFound():String{
        val jokes = listOf<String>("404 Not Found","What can i say","how's everything going?","酸萝卜别吃 这个路由简直O而K之")
        return jokes[Random.nextInt(0,4)]
    }
}