package util

interface Converter {

    fun convert(amount: Double, fromCurrency: Currency, toCurrency: Currency): Double

}