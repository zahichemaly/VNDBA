package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.VN

/**
 * Temporary class used with Rx to zip a VN and Tags in a single object.
 */
data class VNAndTags(
        var vn: VN,
        var tags: Map<Int, Tag>
)