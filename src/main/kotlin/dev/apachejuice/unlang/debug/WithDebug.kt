package dev.apachejuice.unlang.debug

/**
 * Any object with a TagData property for debug purposes.
 */
interface WithDebug {
    /**
     * The tag data. This refers to any data in the context the object was created.
     */
    val tagData: TagData
}