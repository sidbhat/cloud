

import com.oneapp.cloud.core.currency.CurrencyMap
import com.oneapp.cloud.core.currency.ExchangeRate

class CurrencyBootStrap {

    def init = {servletContext ->
        if (!ExchangeRate.list()) {
            def dollar = Currency.getInstance('USD')
            def yen = Currency.getInstance('JPY')
            def euro = Currency.getInstance('EUR')
            def gbp = Currency.getInstance('GBP')

            new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.46122, date: new Date()).save()
            new ExchangeRate(baseCurrency: euro, toCurrency: gbp, rate: 1.33159, date: new Date()).save()
            new ExchangeRate(baseCurrency: yen, toCurrency: dollar, rate: 0.008981, date: new Date()).save()
            new ExchangeRate(baseCurrency: dollar, toCurrency: yen, rate: 111.336, date: new Date()).save()
            new ExchangeRate(baseCurrency: gbp, toCurrency: dollar, rate: 2.02369, date: new Date()).save()


            new CurrencyMap(currency: dollar, description: "US Dollar")
            new CurrencyMap(currency: yen, description: "Yen")
            new CurrencyMap(currency: euro, description: "Euro")
            new CurrencyMap(currency: gbp, description: "Pound")
        }
    }

    def destroy = {
    }
}