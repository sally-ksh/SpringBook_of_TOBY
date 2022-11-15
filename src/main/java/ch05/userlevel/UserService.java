package ch05.userlevel;

import java.util.List;
import java.util.Objects;

public class UserService {
	private final UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public void upgradeLevels() {
		List<User> users = userDao.getAll();

		for (User user : users) {
			Boolean changed = false;
			if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.upgrade(Level.SILVER);
				changed = true;
			} else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
				user.upgrade(Level.GOLD);
				changed = true;
			} else if (user.getLevel() == Level.GOLD) {
				changed = false;
			} else {
				changed = false;
			}
			if (changed) {
				userDao.update(user);
			}
		}
	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.upgrade(Level.BASIC);
		}
		userDao.add(user);
	}
}
