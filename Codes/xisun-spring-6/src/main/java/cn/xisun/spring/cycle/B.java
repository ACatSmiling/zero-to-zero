package cn.xisun.spring.cycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XiSun
 * @since 2023/10/22 11:11
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class B {

    private String name;

    private A a;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "B{" +
                "name='" + name + '\'' +
                ", a=" + a +
                '}';
    }
}
