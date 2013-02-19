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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        // TODO implement
    }

    @Override
    public void updateEndTime(String sessionId, Date endTime)
    {
        // TODO implement
    }
}
