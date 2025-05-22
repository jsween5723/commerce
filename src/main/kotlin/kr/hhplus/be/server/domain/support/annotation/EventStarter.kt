package kr.hhplus.be.server.domain.support.annotation

import kotlin.reflect.KClass

annotation class EventStarter(val event: KClass<*>)
