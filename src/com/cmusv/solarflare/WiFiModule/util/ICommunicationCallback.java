package com.cmusv.solarflare.WiFiModule.util;

import java.util.List;

import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;

public interface ICommunicationCallback {
	public void broadCastUserInfo(List<UserInfo> info);
	public void broadMessage(Message message);
	public void broadCastRemoveUserInfo(List<UserInfo> userInfos);
}
