package com.sc.coding.service

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun DI.MainBuilder.bindServices() {
    bind<FireStoreService>() with singleton { FireStoreService() }
}