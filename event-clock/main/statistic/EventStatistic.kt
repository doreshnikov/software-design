package statistic

interface EventStatistic<S : Any> {

    fun incrementEvent(eventName: String)
    fun getEventStatisticByName(eventName: String): S
    fun getAllEventStatistic(): Map<String, S>
    fun printStatistic()

}