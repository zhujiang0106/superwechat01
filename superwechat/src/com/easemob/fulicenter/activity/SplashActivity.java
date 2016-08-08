package com.easemob.fulicenter.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.db.UserDao;
import com.easemob.fulicenter.task.DownloadCollectCountTask;
import com.easemob.fulicenter.task.DownloadContactsListTask;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private RelativeLayout rootLayout;
	private TextView versionText;
	
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);

		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		versionText = (TextView) findViewById(R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();

					String userName = FuliCenterApplication.getInstance().getUserName();
					UserDao dao = new UserDao(SplashActivity.this);
					UserAvatar user = dao.getUserAvatar(userName);
					if (user == null) {
						final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
						utils.setRequestUrl(I.REQUEST_FIND_USER)
								.addParam(I.User.USER_NAME,userName)
								.targetClass(String.class)
								.execute(new OkHttpUtils2.OnCompleteListener<String>() {
									@Override
									public void onSuccess(String str) {
										Result result = Utils.getResultFromJson(str, UserAvatar.class);
										if (result != null & result.isRetMsg()) {
											UserAvatar user = (UserAvatar) result.getRetData();
											if (user != null) {
												FuliCenterApplication.getInstance().setUser(user);
												FuliCenterApplication.currentUserNick = user.getMUserNick();
											}
										}
									}

									@Override
									public void onError(String error) {
									}
								});
					} else {
						FuliCenterApplication.getInstance().setUser(user);
						FuliCenterApplication.currentUserNick = user.getMUserNick();
					}
					new DownloadContactsListTask(SplashActivity.this,userName).getContacts();
					new DownloadCollectCountTask(SplashActivity.this,userName).getCollectCount();

					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, FuliCenterMainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, FuliCenterMainActivity.class));
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}
}
