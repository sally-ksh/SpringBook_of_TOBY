package ch06.springdynamicproxy;

import java.util.List;
import java.util.Objects;

import ch05.transaction.Level;

public class UserServiceImpl implements UserService {
	private final UserDao userDao;
	private final UserLevelUpgradePolicy userLevelUpgradePolicy;

	private final MailSender mailSender;

	public UserServiceImpl(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, MailSender mailSender) {
		this.userDao = userDao;
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
		this.mailSender = mailSender;
	}

	public void upgradeLevels() {
		upgradeLevelsInternal();
	}

	private void upgradeLevelsInternal() {
		List<User> users = userDao.getAll();

		for (User user : users) {
			if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	private void upgradeLevel(User user) {
		userLevelUpgradePolicy.upgradeLevel(user);
		userDao.update(user);
		sendUpgradeEmail(user);
	}

	private void sendUpgradeEmail(User user) {
		SimpleMailMessage message = new SimpleMailMessageImpl();
		message.setTo(user.getEmail());
		message.setFrom("tobySpring@email.com");
		message.setSubject("upgrade 안내");
		message.setText(user.getName() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드 됐습니다.");

		mailSender.send(message);
	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
