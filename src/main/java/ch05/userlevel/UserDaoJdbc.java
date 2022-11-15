package ch05.userlevel;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;


public class UserDaoJdbc implements UserDao {
	private final JdbcTemplate jdbcTemplate;
	private RowMapper<User> userMapper = (ResultSet resultSet, int rowNum) -> {
		return new User(
			resultSet.getString("id"),
			resultSet.getString("name"),
			resultSet.getString("password"),
			Level.valueOf(resultSet.getInt("level")),
			resultSet.getInt("login"),
			resultSet.getInt("recommend"));};

	public UserDaoJdbc(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// 클래스 -> 로컬클래스 -> 익명 클래스 -> 람다
	public void add(final User user) {
		this.jdbcTemplate.update(
			"insert into users(id,name, password, level, login, recommend) values (?,?,?,?,?,?)",
			user.getId(),
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend());;
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(
			"select id, name, password, level, login, recommend from users where id = ?", new Object[] {id},
			this.userMapper);
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");  // 내장 콜백 사용 메소드 호출
	}

	public int getCount() {
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query(
			"select * from users order by id",
			this.userMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update(
			"update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ? ",
			user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
			user.getId());
	}
}
