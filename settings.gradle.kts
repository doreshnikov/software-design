rootProject.name = "software-design"

include(
    "lru-cache",
    "vk-fetcher",
    "mvc-tasklist",
    "graph-bridge",
    "translator",
    "profiler-aspectj",
    "event-clock"
)

enableFeaturePreview("GRADLE_METADATA")
include("profiler-aspectj")
