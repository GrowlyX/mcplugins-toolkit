package io.liftgate.mcplugins.toolkit.kompat

/*-----------------------------------------------------*\
**                                                     **
**        kompat-hk2: Kompatibility for HK2            **
**        Kotlin Compatibility Project (Kompat)        **
**        Copyright 2017-2018, Alex Westphal           **
**        https://github.com/ahwnz/kompat-hk2          **
**                                                     **
\*-----------------------------------------------------*/
import org.glassfish.hk2.api.ServiceHandle
import org.glassfish.hk2.api.ServiceLocator
import kotlin.reflect.KClass

class NoAvailableServiceException(kClass: KClass<*>): Exception("So available service for ${kClass.simpleName}")

// Get Service

inline fun <reified T> ServiceLocator.getService(vararg qualifiers: Annotation): T =
    this.getService(T::class.java, *qualifiers) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.getService(name: String, vararg qualifiers: Annotation): T =
    this.getService(T::class.java, name, *qualifiers) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.getServiceNullable(vararg qualifiers: Annotation): T? =
    this.getService(T::class.java, *qualifiers)

inline fun <reified T> ServiceLocator.getServiceNullable(name: String, vararg qualifiers: Annotation): T? =
    this.getService(T::class.java, name, *qualifiers)

inline fun <reified T> ServiceLocator.getAllServices(vararg qualifiers: Annotation): List<T> =
    this.getAllServices(T::class.java, *qualifiers)


// Get Service Handle

inline fun <reified T> ServiceLocator.getServiceHandle(vararg qualifiers: Annotation): ServiceHandle<T> =
    this.getServiceHandle(T::class.java, *qualifiers) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.getServiceHandle(name: String, vararg qualifiers: Annotation): ServiceHandle<T> =
    this.getServiceHandle(T::class.java, name, *qualifiers) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.getServiceHandleNullable(vararg qualifiers: Annotation): ServiceHandle<T>? =
    this.getServiceHandle(T::class.java, *qualifiers)

inline fun <reified T> ServiceLocator.getServiceHandleNullable(name: String, vararg qualifiers: Annotation): ServiceHandle<T>? =
    this.getServiceHandle(T::class.java, name, *qualifiers)

inline fun <reified T> ServiceLocator.getAllServiceHandles(vararg qualifiers: Annotation): List<ServiceHandle<T>> =
    this.getAllServiceHandles(T::class.java, *qualifiers)


// Create

inline fun <reified T> ServiceLocator.create(): T = this.create(T::class.java) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.create(strategy: String): T = this.create(T::class.java, strategy) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.createNullable(): T? = this.create(T::class.java)

inline fun <reified T> ServiceLocator.createNullable(strategy: String): T? = this.create(T::class.java, strategy)


// Create and Initialize

inline fun <reified T> ServiceLocator.createAndInitialize(): T = this.createAndInitialize(T::class.java) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.createAndInitialize(strategy: String): T = this.createAndInitialize(T::class.java, strategy) ?: throw NoAvailableServiceException(T::class)

inline fun <reified T> ServiceLocator.createAndInitializeNullable(): T? = this.createAndInitialize(T::class.java)

inline fun <reified T> ServiceLocator.createAndInitializeNullable(strategy: String): T? = this.createAndInitialize(T::class.java, strategy)
