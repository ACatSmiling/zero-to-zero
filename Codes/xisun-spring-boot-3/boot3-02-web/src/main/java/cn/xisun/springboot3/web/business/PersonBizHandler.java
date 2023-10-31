package cn.xisun.springboot3.web.business;

import cn.xisun.springboot3.web.pojos.Person;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiSun
 * @since 2023/10/11 10:52
 */
@Slf4j
@Service
public class PersonBizHandler {

    /**
     * 查询指定id的用户
     *
     * @param request
     * @return
     */
    public ServerResponse getPerson(ServerRequest request) throws Exception {
        String id = request.pathVariable("id");
        log.info("查询 【{}】 用户信息，数据库正在检索", id);
        // 业务处理
        Person person = new Person(1L, "哈哈", "aa@qq.com", 18);
        // 构造响应
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(person);
    }


    /**
     * 获取所有用户
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServerResponse getPersons(ServerRequest request) throws Exception {
        log.info("查询所有用户信息完成");
        // 业务处理
        List<Person> list = Arrays.asList(new Person(1L, "哈哈", "aa@qq.com", 18),
                new Person(2L, "哈哈2", "aa2@qq.com", 12));

        // 构造响应
        return ServerResponse.ok().body(list); // 凡是body中的对象，就是以前@ResponseBody原理，利用HttpMessageConverter写出为JSON
    }


    /**
     * 保存用户
     *
     * @param request
     * @return
     */
    public ServerResponse savePerson(ServerRequest request) throws ServletException, IOException {
        // 提取请求体
        Person body = request.body(Person.class);
        log.info("保存用户信息：{}", body);
        return ServerResponse.ok().build();
    }

    /**
     * 更新用户
     *
     * @param request
     * @return
     */
    public ServerResponse updatePerson(ServerRequest request) throws ServletException, IOException {
        Person body = request.body(Person.class);
        log.info("保存用户信息更新: {}", body);
        return ServerResponse.ok().build();
    }

    /**
     * 删除用户
     *
     * @param request
     * @return
     */
    public ServerResponse deletePerson(ServerRequest request) {
        String id = request.pathVariable("id");
        log.info("删除【{}】用户信息", id);
        return ServerResponse.ok().build();
    }
}
