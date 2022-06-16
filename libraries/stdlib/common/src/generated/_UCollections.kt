/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("UCollectionsKt")

package kotlin.collections

//
// NOTE: THIS FILE IS AUTO-GENERATED by the GenerateStandardLib.kt
// See: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
//

import kotlin.random.*
import kotlin.ranges.contains
import kotlin.ranges.reversed

/**
 * Returns an array of UByte containing all of the elements of this collection.
 */
@SinceKotlin("1.3")
@ExperimentalUnsignedTypes
public fun Collection<UByte>.toUByteArray(): UByteArray {
    val result = UByteArray(size)
    var index = 0
    for (element in this)
        result[index++] = element
    return result
}

/**
 * Returns an array of UInt containing all of the elements of this collection.
 */
@SinceKotlin("1.3")
@ExperimentalUnsignedTypes
public fun Collection<UInt>.toUIntArray(): UIntArray {
    val result = UIntArray(size)
    var index = 0
    for (element in this)
        result[index++] = element
    return result
}

/**
 * Returns an array of ULong containing all of the elements of this collection.
 */
@SinceKotlin("1.3")
@ExperimentalUnsignedTypes
public fun Collection<ULong>.toULongArray(): ULongArray {
    val result = ULongArray(size)
    var index = 0
    for (element in this)
        result[index++] = element
    return result
}

/**
 * Returns an array of UShort containing all of the elements of this collection.
 */
@SinceKotlin("1.3")
@ExperimentalUnsignedTypes
public fun Collection<UShort>.toUShortArray(): UShortArray {
    val result = UShortArray(size)
    var index = 0
    for (element in this)
        result[index++] = element
    return result
}

/**
 * Returns the sum of all elements in the collection.
 */
@kotlin.jvm.JvmName("sumOfUInt")
@SinceKotlin("1.5")
@WasExperimental(ExperimentalUnsignedTypes::class)
public fun Iterable<UInt>.sum(): UInt {
    var sum: UInt = 0u
    for (element in this) {
        sum += element
    }
    return sum
}

/**
 * Returns the sum of all elements in the collection.
 */
@kotlin.jvm.JvmName("sumOfULong")
@SinceKotlin("1.5")
@WasExperimental(ExperimentalUnsignedTypes::class)
public fun Iterable<ULong>.sum(): ULong {
    var sum: ULong = 0uL
    for (element in this) {
        sum += element
    }
    return sum
}

/**
 * Returns the sum of all elements in the collection.
 */
@kotlin.jvm.JvmName("sumOfUByte")
@SinceKotlin("1.5")
@WasExperimental(ExperimentalUnsignedTypes::class)
public fun Iterable<UByte>.sum(): UInt {
    var sum: UInt = 0u
    for (element in this) {
        sum += element
    }
    return sum
}

/**
 * Returns the sum of all elements in the collection.
 */
@kotlin.jvm.JvmName("sumOfUShort")
@SinceKotlin("1.5")
@WasExperimental(ExperimentalUnsignedTypes::class)
public fun Iterable<UShort>.sum(): UInt {
    var sum: UInt = 0u
    for (element in this) {
        sum += element
    }
    return sum
}

