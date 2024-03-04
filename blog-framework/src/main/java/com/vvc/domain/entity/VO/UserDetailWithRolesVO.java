package com.vvc.domain.entity.VO;

import com.vvc.domain.entity.Role;
import com.vvc.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailWithRolesVO {
    private List<String> roleIds;
    private List<Role> roles;
    private User user;
}
