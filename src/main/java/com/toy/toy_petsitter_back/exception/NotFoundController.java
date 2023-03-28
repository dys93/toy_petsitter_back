package com.toy.toy_petsitter_back.exception;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/error"})
public class NotFoundController extends AbstractErrorController {


    @RequestMapping
    @SneakyThrows
    public String error() {
        throw ErrorMessage.NOT_FOUND.getException();
    }

    public NotFoundController() {
        super(new DefaultErrorAttributes());
    }

}
