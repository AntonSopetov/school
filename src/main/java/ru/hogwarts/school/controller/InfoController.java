package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.LongStream;

@RestController
public class InfoController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public String getPort() {
        return port;
    }

    // ШАГ 4: Эндпоинт с оптимизированным параллельным стримом
    @GetMapping("/sum")
    public int getSum() {
        return (int) LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
    }
}
