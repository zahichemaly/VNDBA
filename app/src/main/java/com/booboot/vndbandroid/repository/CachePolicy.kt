@file:Suppress("unused")

package com.booboot.vndbandroid.repository

class CachePolicy<T>(
    /** Cache enabled **/
    var enabled: Boolean = true,
    /** Never sends a network request, only check the cache. Must define a defaultValue in get(). */
    var cacheOnly: Boolean = false,
    /** Cache expiration time in milliseconds (default 10 minutes) **/
    var expiration: Long = if (enabled) 600000 else 0
) {
    private var fetchFromMemory: () -> CacheValue<T> = { CacheValue() }
    private var fetchFromDatabase: () -> CacheValue<T> = { CacheValue() }
    private var fetchFromNetwork: suspend (T?) -> T = { throw NotImplementedError("Implement fetchFromNetwork") }
    private var putInDatabase: (T) -> Unit = { }
    private var putInMemory: (T) -> Unit = { }
    private var isExpired: (T?) -> Boolean = { _ -> false }
    private var putExpiration: (T) -> Unit = { }
    private var isEmpty: (T?) -> Boolean = { it is Collection<*> && it.isEmpty() || it is Map<*, *> && it.isEmpty() }
    private var defaultValue: (T?) -> T = { it ?: throw NotImplementedError("get() with cacheOnly must define a defaultValue !") }

    /**
     * Copying a CachePolicy enables the creation of a new CachePolicy with the same parameters, without the risk of overriding the blocks
     * (in case of parallel requests).
     */
    fun <U> copy() = CachePolicy<U>(enabled = this.enabled)

    fun fetchFromMemory(fetchFromMemory: () -> T?) = apply {
        this.fetchFromMemory = {
            with(fetchFromMemory()) {
                CacheValue(this, requireNonEmpty(this))
            }
        }
    }

    fun fetchFromDatabase(fetchFromDatabase: () -> T?) = apply {
        this.fetchFromDatabase = {
            with(fetchFromDatabase()) {
                CacheValue(this, requireNonEmpty(this)?.apply(putInMemory))
            }
        }
    }

    fun fetchFromNetwork(fetchFromNetwork: suspend (T?) -> T) = apply {
        this.fetchFromNetwork = {
            fetchFromNetwork(it).apply {
                putExpiration(this)
                putInDatabase(this)
                putInMemory(this)
            }
        }
    }

    fun putInMemory(putInMemory: (T) -> Unit) = apply {
        this.putInMemory = putInMemory
    }

    fun putInDatabase(putInDatabase: (T) -> Unit) = apply {
        this.putInDatabase = putInDatabase
    }

    fun putExpiration(putExpiration: (T) -> Unit) = apply {
        this.putExpiration = putExpiration
    }

    fun isExpired(isExpired: (T) -> Boolean) = apply {
        this.isExpired = { it == null || isExpired(it) }
    }

    fun isEmpty(isEmpty: (T) -> Boolean) = apply {
        this.isEmpty = { it == null || isEmpty(it) }
    }

    private fun requireNonEmpty(value: T?) = if (isEmpty(value)) null else value

    suspend fun get(defaultValue: (T?) -> T = this.defaultValue): T = if (enabled) {
        var cache = fetchFromMemory()
        if (cache.checkedValue == null) cache = fetchFromDatabase()

        if (cacheOnly) {
            cache.rawValue ?: defaultValue(cache.rawValue)
        } else with(cache.checkedValue) {
            if (this == null || isExpired(this)) {
                fetchFromNetwork(cache.rawValue)
            } else {
                this
            }
        }
    } else {
        fetchFromNetwork(null)
    }

    private data class CacheValue<T>(
        val rawValue: T? = null, // Raw value as contained in the cache
        val checkedValue: T? = null // Null if isEmpty(), else = rawValue
    )
}