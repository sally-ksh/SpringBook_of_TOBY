package ch05.userlevel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class UserTest {
	private User user;

	@BeforeEach
	void beforeEach() {
		user = new User("sally", "sallysh", "study", Level.BASIC, 1, 0);
	}

	@Test
	void upgradeLevel() {
		Level[] levels = Level.values();
		for (Level level : levels) {
			if (Objects.isNull(level.nextLevel())) continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertThat(user.getLevel() == level.nextLevel()).isTrue();
 		}
	}

	@Test
	void cannotUpgradeLevel() {
		Level[] levels = Level.values();
		for (Level level : levels) {
			if (!Objects.isNull(level.nextLevel())) continue;
			user.setLevel(level);
			assertThrows(IllegalStateException.class, () -> user.upgradeLevel());
		}
	}
}
