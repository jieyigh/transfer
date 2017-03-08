package com.ds.transfer.xiaoyu.vo;

import lombok.Data;

/**
 * ds 登录
 * 
 * @author jackson
 *
 */
@Data
public class LoginVo {

	private String user;

	private String siteId; // 分公司id

	private String pan;

	//股东,总代,代理 (ds,gdm013,zdm013,ddm013)
	private String userTree;

	private String isTest;

	/**
	 * 加密字符串 内容组成为:
	 * SHA1加密(user+siteId+userTree+内部测试私钥)
	 * 内部测试私钥:
	 * llFG8LMBHHGDuaEmqJ12a88Jlm9pD8p4FF5umFE4C7JHK21u6FuGB7qaq9GGh2v837FqNHaFDAN4FBph976g5qGa8NEDMuNE4al3H8Mp9EAL65hA2hpaEh9H
	 */
	private String encrypt;

	public LoginVo(String user, String siteId, String pan, String userTree, String encrypt, String isTest) {
		this.user = user;
		this.siteId = siteId;
		this.pan = pan;
		this.userTree = userTree;
		this.encrypt = encrypt;
		this.isTest = isTest;
	}

}
