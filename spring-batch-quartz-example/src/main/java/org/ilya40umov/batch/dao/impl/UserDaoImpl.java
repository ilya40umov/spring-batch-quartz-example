/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ilya40umov.batch.dao.impl;

import org.ilya40umov.batch.dao.UserDao;
import org.ilya40umov.batch.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ilya40umov
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllUsers()
    {
        return jdbcTemplate.query("SELECT * FROM SBQ_USER", new UserRowMapper());
    }

    @Override
    public User findUserByLogin(String login)
    {
        return jdbcTemplate.queryForObject("SELECT * FROM SBQ_USER WHERE LOGIN = ?", new UserRowMapper(), login);
    }

    public class UserRowMapper implements RowMapper<User>
    {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            User user = new User();
            user.setUserId(rs.getInt("USER_ID"));
            user.setLogin(rs.getString("LOGIN"));
            user.setCreated(rs.getDate("CREATED"));
            return user;
        }
    }

}
