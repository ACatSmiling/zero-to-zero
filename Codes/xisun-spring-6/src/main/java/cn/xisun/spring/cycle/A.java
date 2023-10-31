package cn.xisun.spring.cycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XiSun
 * @since 2023/10/22 11:11
 */
/*@Data
@NoArgsConstructor
@AllArgsConstructor*/
public class A {

    private String name;

    private B b;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                ", b=" + b +
                '}';
    }
}
