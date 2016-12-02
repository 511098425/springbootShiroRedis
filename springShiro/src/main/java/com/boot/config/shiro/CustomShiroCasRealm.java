package com.boot.config.shiro;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.boot.config.mybatis.MyBatisConfig;
import com.boot.entity.SysRole;
import com.boot.entity.SysUser;
import com.boot.mapper.PermissionMapper;
import com.boot.mapper.RoleMapper;
import com.boot.mapper.UserMapper;

/**  
 *
 *项目名称：springShiro
 *
 *描述：自定义shiro的Ream
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月25日 下午2:16:01 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public class CustomShiroCasRealm extends AuthorizingRealm{
	
	// 日志对象
	private static Logger log = LoggerFactory.getLogger(MyBatisConfig.class);
	
	@Autowired
    private UserMapper userMapper; 
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;

/*    @PostConstruct
    public void initProperty(){
//      setDefaultRoles("ROLE_USER");
        setCasServerUrlPrefix(ShiroCasConfiguration.casServerUrlPrefix);
        // 客户端回调地址
        setCasService(ShiroCasConfiguration.shiroServerUrlPrefix + ShiroCasConfiguration.casFilterUrlPattern);
    }*/

    /**
     * 权限认证，为当前登录的Subject授予角色和权限 
     * @see 经测试：本例中该方法的调用时机为需授权资源被访问时 
     * @see 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache 
     * @see 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("==================================执行Shiro权限认证=========================================");
        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String)super.getAvailablePrincipal(principalCollection); 
        SysUser su=new SysUser();
        su.setUsername(loginName);
        //到数据库查是否有此对象
        SysUser user=userMapper.selectOne(su);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        if(user!=null){
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色集合
            Set<String> roleNames=roleMapper.selectUserRole(user.getId());
            info.setRoles(roleNames);
            //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            List<SysRole> roleList=roleMapper.selectRoles(user.getId());
            for (SysRole role : roleList) {
            	Set<String> permissions=permissionMapper.selectRolePermission(role.getId());
                info.addStringPermissions(permissions);
            }
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }
    
    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;

        log.info("验证当前Subject时获取到token为：" + token); 

        //查出是否有此用户
        SysUser su=new SysUser();
        su.setUsername(token.getUsername());
        //到数据库查是否有此对象
        SysUser user=userMapper.selectOne(su);
        if(user == null){
        	 throw new UnknownAccountException();//账户不存在
        }
        if(user.getStatus()==1){
        	throw new LockedAccountException(); //帐号锁定
        }
        SimpleAuthenticationInfo authenticationInfo=null;
        if(user!=null){
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
        	authenticationInfo=new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),ByteSource.Util.bytes(user.getCredentialsSalt()),getName());
        }
        return authenticationInfo;
    }
}
  