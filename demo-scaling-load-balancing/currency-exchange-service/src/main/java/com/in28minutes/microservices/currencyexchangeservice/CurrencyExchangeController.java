package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to

    ) {
        String port = env.getProperty("local.server.port");
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        currencyExchange.setEnvironment(port);
        if (currencyExchange == null) {
            throw new RuntimeException("Unable to Find data for" + from + " to " + to);
        }
        return currencyExchange;
    }
}
