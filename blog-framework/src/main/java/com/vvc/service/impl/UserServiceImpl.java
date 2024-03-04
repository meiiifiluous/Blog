package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Role;
import com.vvc.domain.entity.User;
import com.vvc.domain.entity.UserRole;
import com.vvc.domain.entity.VO.PageVo;
import com.vvc.domain.entity.VO.UserDetailWithRolesVO;
import com.vvc.domain.entity.VO.UserInfoVo;
import com.vvc.exception.SystemException;
import com.vvc.mapper.UserMapper;
import com.vvc.service.RoleService;
import com.vvc.service.UserRoleService;
import com.vvc.service.UserService;
import com.vvc.utils.BeanCopyUtils;
import com.vvc.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-19 13:32:58
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!Objects.isNull(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());
        if(StringUtils.hasText(user.getPhonenumber())&&(user.getPhonenumber().length()!=11)){
            return ResponseResult.errorResult(500,"手机号长度为11位！");
        }
        queryWrapper.eq(!Objects.isNull(user.getStatus()),User::getStatus,user.getStatus());
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage,queryWrapper);
        return ResponseResult.okResult(new PageVo(userPage.getRecords(),userPage.getTotal()));
    }

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;
    @Override
    public ResponseResult add(User user) {
        if(Objects.isNull(user.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.notIn(Role::getRoleKey,"common");
        List<Role> roleKeys = roleService.list(queryWrapper);
        if(roleKeys.contains(user.getRoleIds())){
            user.setType("1");
        }else user.setType("0");
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
        List<UserRole> userRoles = user.getRoleIds().stream().map(roleId->new UserRole(user.getId(),Long.valueOf(roleId)))
                        .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserDetailWithRoles(Long id) {
        UserDetailWithRolesVO userDetailWithRolesVO = new UserDetailWithRolesVO();
        //得到rolesId
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<String> roleIds = userRoles.stream().map(userRole -> userRole.getRoleId().toString()).collect(Collectors.toList());
        userDetailWithRolesVO.setRoleIds(roleIds);
        //得到所有角色的列表
        List<Role> roles = roleService.list();
        userDetailWithRolesVO.setRoles(roles);
        //得到用户信息，返回userVo
        User user = getById(id);
        userDetailWithRolesVO.setUser(user);
        return ResponseResult.okResult(userDetailWithRolesVO);
    }

    @Override
    public ResponseResult updateUserWithRoles(User user) {
        List<String> roleIds = user.getRoleIds();
        //先删除
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);
        //在保存
        List<UserRole> userRoles = roleIds.stream().map(roleId -> new UserRole(user.getId(), Long.valueOf(roleId))).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
}

