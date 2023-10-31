package cn.xisun.springboot3.web.pojos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XiSun
 * @since 2023/10/6 14:35
 */
@JacksonXmlRootElement
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private Long id;

    private String userName;

    private String email;

    private Integer age;

}
