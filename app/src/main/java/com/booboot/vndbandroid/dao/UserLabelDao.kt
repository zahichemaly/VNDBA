package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.UserLabel
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class UserLabelDao() {
    @Id(assignable = true) var id: Long = 0
    var label: String = ""
    var _private: Boolean = false

    constructor(userLabel: UserLabel) : this() {
        id = userLabel.id
        label = userLabel.label
        _private = userLabel.private
    }

    fun toBo() = UserLabel(
        id,
        label,
        _private
    )
}