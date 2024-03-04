package com.vvc.domain.entity.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
//开启链式返回对象本身
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;
}
