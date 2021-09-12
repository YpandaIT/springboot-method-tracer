package indi.yp.tracer.demo.controller;

import indi.yp.tracer.demo.service.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/")
public class TracerController {
    @Autowired
    private ServiceA serviceA;

    @GetMapping("tracer")
    public String tracer() {
        serviceA.syncMethod();
        return "success";
    }

}
