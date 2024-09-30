package com.codegenetics.extensions.helper


typealias IntCallback = (Int) -> Unit
typealias LongCallback = (Long) -> Unit
typealias StringCallback = (String) -> Unit
typealias BooleanCallback = (Boolean) -> Unit
typealias SimpleCallback = () -> Unit
typealias AnyCallback = (Any?) -> Unit

typealias GenericCallback<T> = (T) -> Unit
typealias PairCallback<T> = (Pair<T,T>) -> Unit