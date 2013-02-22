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
package org.sbq.batch.domain;

import java.util.Date;

/**
 * @author ilya40umov
 */
public class UserSession
{
    private int userSessionId;
    private int userId;
    private String sessionId;
    private Date startTime;
    private Date endTime;

    public UserSession()
    {

    }

    public UserSession(int userId, String sessionId, Date startTime, Date endTime)
    {
        this.userId = userId;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getUserSessionId()
    {
        return userSessionId;
    }

    public void setUserSessionId(int userSessionId)
    {
        this.userSessionId = userSessionId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
}
