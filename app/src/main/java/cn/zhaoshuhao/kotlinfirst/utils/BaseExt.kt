package cn.zhaoshuhao.kotlinfirst.utils

/**
 * Created by Scout
 * Created on 2017/8/12 16:20.
 */
/*函数的特殊调用方法*/
fun <P1, P2, R> ((P1, P2) -> R).componet() = fun(p1: P1) = fun(p2: P2) = this(p1, p2)

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).componet() = fun(p1: P1) = fun(p2: P2) = fun(p3: P3) = this(p1, p2, p3)

fun <P1, P2, P3, P4, R> ((P1, P2, P3, P4) -> R).componet() = fun(p1: P1) = fun(p2: P2) = fun(p3: P3) = fun(p4: P4) = this(p1, p2, p3, p4)

/*偏函数使用*/
fun <P1, P2, R> Function2<P1, P2, R>.compose(p1: P1) = fun(p2: P2) = this(p1, p2)

fun <P1, P2, P3, R> Function3<P1, P2, P3, R>.compose(p1: P1) = fun(p2: P2) = fun(p3: P3) = this(p1, p2, p3)

/*函数组合*/
infix fun <P1, P2, R> Function1<P1, P2>.andThen(function: Function1<P2, R>) = fun(p1: P1) {
    function.invoke(this(p1))
}

//inline fun <T, R> Any?.withNonNull(receiver: T, block: T.() -> R): R? {
//    return receiver?.block()
//}
