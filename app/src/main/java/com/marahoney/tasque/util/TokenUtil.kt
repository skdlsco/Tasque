/*
 * Created by 나인채 on 5/26/2019
 * Copyright (c) 2019. All rights reversed
 */

package com.marahoney.tasque.util

import java.util.*

object TokenUtil {
    val newToken: String get() = UUID.randomUUID().toString().split("-").subList(1, 5).joinToString("")
}