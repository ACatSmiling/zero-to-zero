package cn.xisun.springboot3.web.controllers;

import cn.xisun.springboot3.web.pojos.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XiSun
 * @since 2023/10/6 15:36
 */
@RestController
public class PersonController {

    @GetMapping("/person")
    public Person getPerson() {
        return new Person(1L, "张三", "123@qq.com", 18);
    }
}
