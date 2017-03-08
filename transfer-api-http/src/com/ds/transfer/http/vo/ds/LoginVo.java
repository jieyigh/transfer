package com.ds.transfer.http.vo.ds;

/**
 * ds登录参数
 * 
 * @author jackson
 *
 */
public class LoginVo {

	private String hashCode;
	private String command;
	private Param params;

	public LoginVo() {
	}

	public LoginVo(String hashCode, String command) {
		this.hashCode = hashCode;
		this.command = command;
	}

	public LoginVo(String hashCode, String command, Param params) {
		this.hashCode = hashCode;
		this.command = command;
		this.params = params;
	}

	public class Param {
		private String username;//合作伙伴平台的用户ID,字符串长度少于或者等于20位, 由字母和数字组成
		private String password;//合作伙伴平台的用户原始密码，要求ＭＤ５加密, 长度为32位
		private String currency;//合作伙伴的用户ID所使用的币别
		private String nickname;//合作伙伴的用户昵称, 字符串长度少于或者等于20位
		private String language;//游戏语言(CN,HK,EN,TH,VN)|(CN,EN)
		private int line;//线路(可选参数) 　参数(1, 2, …)  默认线路为1, 注:具体线路参数ID以游戏平台为准
		private String gameType;// 游戏参数为香港彩（LOTTO）|香港彩（LOTTERY）.注：不能为空。
		private String userCode;//玩家在现金网的唯一标识。不能为空

		private String lottoTray;//香港彩盘口可选择（A,B,C,D）| ，注: 不能为空.
		private String lotteryTray;//时时彩盘口可选择（A,B,C）

		private String lottoType;// 香港彩:登入手机版还是网页版，可以为空，如果空游戏自动判断是网页版还是手机版，值为（PC，MP）注：PC 为网页版游戏界面，MP 为手机版游戏界面。
		private String lotteryType;//时时彩:

		/** 大厅登录 */
		public Param(String username, String password, String currency, String nickname, String language, int line, String userCode) {
			this.username = username;
			this.password = password;
			this.currency = currency;
			this.nickname = nickname;
			this.language = language;
			this.line = line;
			this.userCode = userCode;
		}

		/** 时时彩,香港彩赋值 */
		public Param(String username, String password, String currency, String nickname, String language, int line, String gameType, String lottoTray, String userCode, String lottoType, boolean isLotto) {
			this.username = username;
			this.password = password;
			this.currency = currency;
			this.nickname = nickname;
			this.language = language;
			this.line = line;
			this.gameType = gameType;
			this.userCode = userCode;
			if (isLotto) {
				this.lottoTray = lottoTray;
				this.lottoType = lottoType;
			} else {
				this.lotteryTray = lottoTray;
				this.lotteryType = lottoType;
			}
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public String getGameType() {
			return gameType;
		}

		public void setGameType(String gameType) {
			this.gameType = gameType;
		}

		public String getLottoTray() {
			return lottoTray;
		}

		public void setLottoTray(String lottoTray) {
			this.lottoTray = lottoTray;
		}

		public String getUserCode() {
			return userCode;
		}

		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}

		public String getLottoType() {
			return lottoType;
		}

		public void setLottoType(String lottoType) {
			this.lottoType = lottoType;
		}

		public String getLotteryType() {
			return lotteryType;
		}

		public void setLotteryType(String lotteryType) {
			this.lotteryType = lotteryType;
		}

		public String getLotteryTray() {
			return lotteryTray;
		}

		public void setLotteryTray(String lotteryTray) {
			this.lotteryTray = lotteryTray;
		}

	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Param getParams() {
		return params;
	}

	public void setParams(Param params) {
		this.params = params;
	}

}
