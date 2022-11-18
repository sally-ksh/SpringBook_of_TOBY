package ch06.separateTransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import ch05.transaction.Level;

public class UserServiceImpl implements UserService {
	public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	private final UserDao userDao;
	private final UserLevelUpgradePolicy userLevelUpgradePolicy;

	private final MailSender mailSender;

	public UserServiceImpl(DataSource dataSource, PlatformTransactionManager transactionManager,
		UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, MailSender mailSender) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
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

/*		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.ksug.org");
		Session session = Session.getInstance(props, null);

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("email@email.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("upgrade 안내", "UTF-8");
			message.setText(user.getName() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드 됐습니다.");

			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}*/
	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
