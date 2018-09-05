package studio.vifi.hknews.data.model

enum class StoryType {
    NEW, TOP, BEST, ASK, SHOW, JOB;

    fun requestPath(): String {
        return "${name.toLowerCase()}stories.json"
    }
}