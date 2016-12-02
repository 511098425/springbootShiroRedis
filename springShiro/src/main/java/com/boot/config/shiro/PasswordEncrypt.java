package com.boot.config.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.boot.entity.SysUser;

/**  
 *
 *项目名称：springShiro
 *
 *描述：密码加密
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月28日 上午11:00:45 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public class PasswordEncrypt {
	 private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	    private String algorithmName = "md5";
	    private int hashIterations = 2;
	    
	    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
	        this.randomNumberGenerator = randomNumberGenerator;
	    }
	    
	    public void setAlgorithmName(String algorithmName) {
	        this.algorithmName = algorithmName;
	    }

	    public void setHashIterations(int hashIterations) {
	        this.hashIterations = hashIterations;
	    }
	    /**
	     * 加密生成密码
	     * @param user
	     */
	    public void encryptPassword(SysUser user) {

	        user.setSalt(randomNumberGenerator.nextBytes().toHex());
	        String newPassword = new SimpleHash(
	                algorithmName,
	                user.getPassword(),
	                ByteSource.Util.bytes(user.getCredentialsSalt()),
	                hashIterations).toHex();
	        user.setPassword(newPassword);
	    }
	/*    public static void main(String[] args) {
			SysUser user=new SysUser();
			user.setUsername("rose");
			user.setPassword("123456");
			PasswordEncrypt pe=new PasswordEncrypt();
			pe.encryptPassword(user);
			System.out.println("====="+user.getPassword()+"====="+user.getSalt());
		}*/
}
  