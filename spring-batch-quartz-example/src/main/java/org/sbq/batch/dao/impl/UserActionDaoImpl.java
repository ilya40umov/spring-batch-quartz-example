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
package org.sbq.batch.dao.impl;

import org.sbq.batch.dao.UserActionDao;
import org.sbq.batch.domain.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ilya40umov
 */
@Repository
@Transactional
public class UserActionDaoImpl implements UserActionDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(UserAction userAction)
    {
        String sql = "INSERT INTO SBQ_USER_ACTION (USER_SESSION_ID, ACTION, CREATED) VALUES (?, ?, NOW())";
        jdbcTemplate.update(sql, new Object[]{userAction.getUserSessionId(), userAction.getAction().toString()});
    }
}
