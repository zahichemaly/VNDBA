package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Character(
    var id: Int = 0,
    var name: String = "",
    var original: String? = null,
    var _gender: String? = null,
    var _bloodt: String? = null,
    var birthday: IntArray = IntArray(2),
    var aliases: String? = null,
    var description: String? = null,
    var image: String? = null,
    var bust: Int = 0,
    var waist: Int = 0,
    var hip: Int = 0,
    var height: Int = 0,
    var weight: Int = 0,
    var traits: List<IntArray> = emptyList(),
    var vns: List<Array<Any>> = emptyList(),
    var voiced: List<CharacterVoiced> = emptyList()
) {
    var gender = _gender
        set(gender) {
            field = gender?.toUpperCase() ?: gender
        }
    var bloodt = _bloodt
        set(bloodt) {
            field = bloodt?.toUpperCase() ?: bloodt
        }

    companion object {
        val ROLES = LinkedHashMap<String, String>()
        var ROLES_KEYS: List<String>
        val ROLE_INDEX = 3

        init {
            /* Keep the order here : characters' roles are sorted in the same order as below */
            ROLES.put("main", "Protagonist")
            ROLES.put("primary", "Main character")
            ROLES.put("side", "Side character")
            ROLES.put("appears", "Makes an appearance")

            ROLES_KEYS = ArrayList(ROLES.keys)
        }
    }
}
