package com.booboot.vndbandroid.repository

class CachePolicy<T>(
    /** Cache enabled **/
    var enabled: Boolean = true,
    /** Cache expiration time in milliseconds (default 10 minutes) **/
    var expiration: Long = if (enabled) 600000 else 0
) {
    private var fetchFromMemory: () -> T? = { throw NotImplementedError("Implement fetchFromMemory") }
    private var fetchFromDatabase: () -> T? = { null }
    private var fetchFromNetwork: (T?) -> T = { throw NotImplementedError("Implement fetchFromNetwork") }
    private var putInDatabase: (T) -> Unit = { }
    private var putInMemory: (T) -> Unit = { }
    private var isExpired: (T) -> Boolean = { _ -> false }
    private var putExpiration: (T) -> Unit = { }
    private var isEmpty: (T) -> Boolean = { it is Collection<*> && it.isEmpty() || it is Map<*, *> && it.isEmpty() }

    /**
     * Copying a CachePolicy enables the creation of a new CachePolicy with the same parameters, without the risk of overriding the blocks
     * (in case of parallel requests).
     */
    fun <U> copy() = CachePolicy<U>(enabled = this.enabled)

    fun fetchFromMemory(fetchFromMemory: () -> T?) = apply {
        this.fetchFromMemory = {
            requireNonEmpty(fetchFromMemory())
        }
    }

    fun fetchFromDatabase(fetchFromDatabase: () -> T?) = apply {
        this.fetchFromDatabase = {
            requireNonEmpty(fetchFromDatabase())?.apply { putInMemory(this) }
        }
    }

    fun fetchFromNetwork(fetchFromNetwork: (T?) -> T) = apply {
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
        this.isExpired = isExpired
    }

    fun isEmpty(isEmpty: (T) -> Boolean) = apply {
        this.isEmpty = isEmpty
    }

    private fun requireNonEmpty(value: T?) = with(value) {
        if (this != null && !isEmpty(this)) {
            this
        } else null
    }

    fun get(): T = if (enabled) {
        val cache = fetchFromMemory() ?: fetchFromDatabase()

        if (cache == null || isExpired(cache)) {
            fetchFromNetwork(cache)
        } else {
            cache
        }
    } else {
        fetchFromNetwork(null)
    }
}