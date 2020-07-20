package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.QUserRole;
import com.gk4u.rss.backend.model.User;
import com.gk4u.rss.backend.model.UserRole;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class UserRoleDAO extends GenericDAO<UserRole> {

	private QUserRole role = QUserRole.userRole;

	@Inject
	public UserRoleDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<UserRole> findAll() {
		return query().selectFrom(role).leftJoin(role.user).fetchJoin().distinct().fetch();
	}

	public List<UserRole> findAll(User user) {
		return query().selectFrom(role).where(role.user.eq(user)).distinct().fetch();
	}

	public Set<UserRole.Role> findRoles(User user) {
		return findAll(user).stream().map(r -> r.getRole()).collect(Collectors.toSet());
	}
}
