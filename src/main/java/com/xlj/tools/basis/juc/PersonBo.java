package com.xlj.tools.basis.juc;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XLJ
 * @date 2020/10/24
 */
@NoArgsConstructor
@Data
public class PersonBo {
    public Integer id;
    public String name;

    public PersonBo(String name) {
        this.name = name;
    }

    public PersonBo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
