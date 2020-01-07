package com.nsc.Amoski.chlientTest;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "eureka")
public class YmlConfingValue {
    private Map<String, String> instance = new HashMap<>();
    private Map<String, String> client = new HashMap<>();

}
