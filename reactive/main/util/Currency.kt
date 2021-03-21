package util

enum class Currency(val longName: String, val relativeValue: Double) {

    RUB("Russian Rubles", 1.00),
    USD("US Dollars", 72.86),
    EUR("Euros", 86.98),
    JPY("Japanese Yen", 0.67);

    companion object : Converter {
        override fun convert(amount: Double, fromCurrency: Currency, toCurrency: Currency): Double {
            return amount * fromCurrency.relativeValue / toCurrency.relativeValue
        }
    }

}