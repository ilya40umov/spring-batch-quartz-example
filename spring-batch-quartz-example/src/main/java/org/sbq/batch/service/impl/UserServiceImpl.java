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
package org.sbq.batch.service.impl;

import org.sbq.batch.dao.UserActionDao;
import org.sbq.batch.dao.UserDao;
import org.sbq.batch.dao.UserSessionDao;
import org.sbq.batch.domain.User;
import org.sbq.batch.domain.UserAction;
import org.sbq.batch.domain.UserSession;
import org.sbq.batch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author ilya40umov
 */
@Service
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private UserActionDao userActionDao;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers()
    {
        return userDao.findAllUsers();
    }

    @Override
    public String login(String login)
    {
        Date now = new Date();
        User user = userDao.findUserByLogin(login);
        if (user != null)
        {
            String sessionId = UUID.randomUUID().toString();
            userSessionDao.insert(new UserSession(user.getUserId(), sessionId, now, null));
            int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
            userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.DO_LOGIN, now));
            return sessionId;
        } else
        {
            throw new IllegalArgumentException("User with a such login is not found!");
        }
    }

    @Override
    public void logout(String sessionId)
    {
        Date now = new Date();
        int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
        userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.DO_LOGOUT, now));
        userSessionDao.updateEndTime(sessionId, now);
    }

    @Override
    public void goWalking(String sessionId)
    {
        Date now = new Date();
        int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
        userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.GO_WALKING, now));
    }

    @Override
    public void goChatting(String sessionId)
    {
        Date now = new Date();
        int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
        userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.GO_CHATTING, now));
    }

    @Override
    public void goDancing(String sessionId)
    {
        Date now = new Date();
        int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
        userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.GO_DANCING, now));
    }

    @Override
    public void goIdle(String sessionId)
    {
        Date now = new Date();
        int userSessionId = userSessionDao.findUserSessionBySessionId(sessionId).getUserSessionId();
        userActionDao.insert(new UserAction(userSessionId, UserAction.ActionType.GO_IDLE, now));
    }
}
