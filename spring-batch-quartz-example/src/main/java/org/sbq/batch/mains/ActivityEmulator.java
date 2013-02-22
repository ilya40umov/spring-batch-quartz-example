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
package org.sbq.batch.mains;

import org.sbq.batch.domain.User;
import org.sbq.batch.configurations.ActivityEmulatorConfiguration;
import org.sbq.batch.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ilya40umov
 */
public class ActivityEmulator
{
    private final BlockingQueue<Runnable> blockingQueue;
    private final ExecutorService executor;
    private final ApplicationContext appCtx;
    private final UserService userService;
    private final Map<String, AtomicBoolean> userStatusByLogin;

    private ActivityEmulator()
    {
        super();
        blockingQueue = new LinkedBlockingQueue<Runnable>();
        executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, blockingQueue);
        appCtx = new AnnotationConfigApplicationContext(ActivityEmulatorConfiguration.class);
        userService = appCtx.getBean(UserService.class);
        Map<String, AtomicBoolean> writableUserStatusByLogin = new HashMap<String, AtomicBoolean>();
        for (User user : userService.findAllUsers())
        {
            writableUserStatusByLogin.put(user.getLogin(), new AtomicBoolean(false));
        }
        userStatusByLogin = Collections.unmodifiableMap(writableUserStatusByLogin);
    }

    public static void main(String[] vars) throws IOException
    {
        System.out.println("ActivityEmulator - STARTED.");
        ActivityEmulator activityEmulator = new ActivityEmulator();
        activityEmulator.begin();
        System.out.println("Enter your command: >");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String command = null;
        while (!"stop".equals(command = in.readLine()))
        {
            if ("status".equals(command))
            {
                List<String> onlineUsers = new LinkedList<String>();
                for (Map.Entry<String, AtomicBoolean> entry : activityEmulator.getUserStatusByLogin().entrySet())
                {
                    if (entry.getValue().get())
                    {
                        onlineUsers.add(entry.getKey());
                    }
                }
                System.out.println("Users online: " + Arrays.toString(onlineUsers.toArray()));
                System.out.println("Number of cycles left: " + activityEmulator.getBlockingQueue().size());

            }
            System.out.println("Enter your command: >");
        }
        activityEmulator.stop();
        System.out.println("ActivityEmulator - STOPPED.");
    }

    private void begin()
    {
        for (int i = 0; i < 100000; i++)
        {
            executor.execute(new UserEmulator());
        }
    }

    private void stop()
    {
        executor.shutdownNow();
    }

    public BlockingQueue<Runnable> getBlockingQueue()
    {
        return blockingQueue;
    }

    public ExecutorService getExecutor()
    {
        return executor;
    }

    public Map<String, AtomicBoolean> getUserStatusByLogin()
    {
        return userStatusByLogin;
    }

    private final class UserEmulator implements Runnable
    {
        private final Random rnd = new Random();

        @Override
        public void run()
        {
            String login = acquireRandomOfflineUser();
            try
            {
                String sessionId = userService.login(login);
                sleep(rnd.nextInt(10) * 100L);
                for (int i = 0, n = rnd.nextInt(5); i < n; i++)
                {
                    switch (rnd.nextInt(4))
                    {
                        case 0:
                            userService.goChatting(sessionId);
                            break;
                        case 1:
                            userService.goDancing(sessionId);
                            break;
                        case 2:
                            userService.goIdle(sessionId);
                            break;
                        case 3:
                            userService.goWalking(sessionId);
                            break;
                    }
                    sleep(rnd.nextInt(10) * 100L);
                }
                sleep(rnd.nextInt(10) * 100L);
                userService.logout(sessionId);
            } finally
            {
                releaseUser(login);
            }
        }

        private String acquireRandomOfflineUser()
        {
            while (true)
            {
                int nextIdx = rnd.nextInt(userStatusByLogin.values().size());
                int i = 0;
                for (Map.Entry<String, AtomicBoolean> entry : userStatusByLogin.entrySet())
                {
                    i++;
                    if (i >= nextIdx)
                    {
                        String login = entry.getKey();
                        AtomicBoolean status = entry.getValue();
                        if (!status.get())
                        {
                            if (status.compareAndSet(false, true))
                            {
                                return login;
                            }
                        }
                    }
                }
            }
        }

        private void releaseUser(String login)
        {
            userStatusByLogin.get(login).set(false);
        }

        private void sleep(long delay)
        {
            try
            {
                Thread.sleep(delay);
            } catch (InterruptedException e)
            {
                Thread.interrupted();
            }
        }
    }

}
