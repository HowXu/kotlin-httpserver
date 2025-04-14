package cn.howxu.http

import java.io.BufferedReader

/**@author HowXu
 *
 * Http请求类型 默认不进行初始化
 */
data class HttpRequest(
    val category: Category,
    val route_path: String,
    val version: Version,
    val body: String,
    val headers: Map<String, String>
) {
    companion object Functions {
        /**@author HowXu
         *
         * 解构传入的包请求
         *
         */
        fun parse(reader: BufferedReader): HttpRequest {
            // 默认初始化值
            var category = Category.UNDEFINED
            var route_path = ""
            var version = Version.UNDEFINED
            var headers = mutableMapOf<String, String>()
            var body = ""
            /*
            mutableMapOf 是 Kotlin 标准库中的一个函数，用于创建一个可变的 MutableMap 集合。
            基本概念
            可变映射：与 mapOf 创建的不可变映射不同，mutableMapOf 创建的映射可以添加、删除或修改键值对
            接口实现：返回的是 MutableMap 接口的实现，通常是 LinkedHashMap
            初始内容：可以传入初始的键值对作为参数
             */
            val builder = StringBuilder()

            // 经典JavaBufferedReader读入操作
            var r: String? // 跳过初始化
            /*
            由于可空类型可能为 null，Kotlin 提供了几种安全操作方式：

            安全调用操作符 ?.
            val length = nullable?.length  // 如果nullable为null，返回null而不是抛出异常

            Elvis 操作符 ?:
            val length = nullable?.length ?: 0  // 如果为null，使用默认值0

            非空断言 !!
            val length = nullable!!.length  // 确信不为null，如果为null会抛出NPE

            安全类型转换 as?
            val anyObj: Any = "Hello"
            val str: String? = anyObj as? String  // 如果转换失败返回null
            */

            /** 返回 String!代表可能为空 实际上是看不到的
             *  .also 对接收者对象执行给定代码块，然后返回对象本身 lambda函数入参readline的返回值 默认参数名it
             *  因为前面一定是!= null 所以后面可以直接非空断言
             */

            while (reader.readLine().also { r = it } != null && r!!.isNotEmpty()) {
                builder.append(r).append("\n")
            }

            // 读取完成开始解析字符串 .line自动返回行的数组
            builder.lines().forEachIndexed { index, s ->
                if (index == 0) {
                    val sped = s.split(" ")
                    category = Category.parse(sped[0])
                    route_path = sped[1]
                    version = Version.parse(sped[2])
                }
                // 解析第一行获得主要数据之后 解析请求头和请求体 有: 的一定是请求头
                else if (s.contains(": ")) {
                    val sped = s.split(": ")
                    // 考虑可能多含 传入键值对
                    headers[sped[0]] = sped[1]
                }
                // 然后一定是一个空行
                else if (s.isEmpty()) {
                }
                // 最后处理请求请求体
                else {
                    body = s
                }
            }

            // 构建HttpRequest
            return HttpRequest(category, route_path, version, body, headers)
        }
    }
}

/**@author HowXu
 * Http返回类型
 */
data class HttpResponse(
    val version: Version = Version.HTTP_1_1,
    val statusCode: Int,
    val statusText: String,
    val headers: Map<String, String> = emptyMap(),
    val responseText: String = ""
) {
    /**@author HowXu
     * 重写toString返回
     */
    override fun toString(): String {
        // 高贵的多行文本 高贵的上下文提取变量
        // 中间的空行不能省略
        return """
            $version $statusCode $statusText
            ${headers.map { "${it.key}: ${it.value}" }.joinToString("\n")}
            
            $responseText
        """.trimIndent()
    }
}