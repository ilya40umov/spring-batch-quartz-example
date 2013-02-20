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

import org.ilya40umov.batch.dao.UserSessionDao;
import org.ilya40umov.batch.domain.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author ilya40umov
 */
@Repository
@Transactional
public class UserSessionDaoImpl implements UserSessionDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(UserSession userSession)
    {
        String sql = "INSERT INTO SBQ_USER_SESSION (USER_ID, SESSION_ID, START_TIME) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{userSession.getUserId(), userSession.getSessionId(),
                new java.sql.Date(userSession.getStartTime().getTime())
        });
    }

    @Override
    public UserSession findUserSessionBySessionId(String sessionId)
    {
        return jdbcTemplate.queryForObject("SELECT * FROM SBQ_USER_SESSION WHERE SESSION_ID = ?", new UserSessionRowMapper(), sessionId);
    }

    @Override
    public void updateEndTime(String sessionId, Date endTime)
    {
        String sql = "UPDATE SBQ_USER_SESSION SET END_TIME = ? WHERE SESSION_ID = ?";
        jdbcTemplate.update(sql, new Object[]{endTime, sessionId});
    }

    public class UserSessionRowMapper implements RowMapper<UserSession>
    {
        public UserSession mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            UserSession userSession = new UserSession();
            userSession.setUserSessionId(rs.getInt("USER_SESSION_ID"));
            userSession.setUserId(rs.getInt("USER_ID"));
            userSession.setSessionId(rs.getString("SESSION_ID"));
            userSession.setStartTime(rs.getDate("START_TIME"));
            userSession.setEndTime(rs.getDate("END_TIME"));
            return userSession;
        }
    }
}
