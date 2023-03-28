package com.toy.toy_petsitter_back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    public final Logger logger = LoggerFactory.getLogger("Toy-UserService");


    public Logger getLog() {
        return this.logger;
    }

    public LogService() {}

}
