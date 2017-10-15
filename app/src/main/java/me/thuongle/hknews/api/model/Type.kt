package me.thuongle.hknews.api.model

enum class Type(val description: String) {

    STORY("story"),
    COMMENT("comment"),
    ASK("ask"),
    JOB("job"),
    POLL("poll"),
    POLLOPT("pollopt")
}
