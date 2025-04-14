package cn.howxu.http

import cn.howxu.http.Category.*

/**@author HowXu
 *
 * 三种请求类型
 */
enum class Category {
    GET,
    POST,
    UNDEFINED;

    //请求类型的解析方法应该是派生的
    companion object Functions {
        /**@author HowXu
         *
         * 这里用来解析输入的类型返回请求类型 格式化函数
         */
        fun parse(content: String): Category {

            return when (content) {
                "GET", "Get", "get" -> GET
                "POST", "Post", "post" -> POST
                else -> UNDEFINED
            }
        }
    }
}

/**@author HowXu
 *
 * 协议版本
 */
enum class Version {
    HTTP_1_1,
    UNDEFINED;

    companion object Functions {
        /**@author HowXu
         *
         * 解析返回HTTP协议版本
         * 展示另外一种简洁的函数写法
         */
        fun parse(content: String): Version = when (content) {
            "HTTP/1.1" -> HTTP_1_1
            else -> UNDEFINED
        }
    }

    /**@author HowXu
     *
     * 重写toString方法
     */
    override fun toString(): String = when (this) {
        HTTP_1_1 -> "HTTP/1.1"
        else -> "UNDEFINED"
    }

}

