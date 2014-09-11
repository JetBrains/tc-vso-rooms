/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.vsoRooms.notificator;

import com.intellij.openapi.util.JDOMUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jetbrains.buildServer.configuration.ChangeListener;
import jetbrains.buildServer.configuration.FileWatcher;
import jetbrains.buildServer.notification.FreeMarkerHelper;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.vsoRooms.Constants;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificatorConfig implements ChangeListener {

  private static final Logger LOG = Logger.getLogger(VSONotificatorConfig.class);

  private static final String ACCOUNT = "account";
  private static final String USER = "user";
  private static final String PASSWORD = "password";
  private static final String PAUSED = "paused";

  private final File myConfigFile;
  private final FileWatcher myChangeObserver;
  private final Configuration myConfiguration;

  private String myAccount;
  private String myUser;
  private String myPassword;
  private boolean myPaused;

  public VSONotificatorConfig(@NotNull ServerPaths serverPaths, @NotNull SBuildServer server) throws IOException {
    final File configDir = new File(serverPaths.getConfigDir(), FreeMarkerHelper.TEMPLATES_ROOT + "/" + Constants.NOTIFICATOR_TYPE);
    myConfigFile = new File(configDir, Constants.CONFIG_FILENAME);

    reloadConfiguration();

    myChangeObserver = new FileWatcher(myConfigFile);
    myChangeObserver.setSleepingPeriod(10000);
    myChangeObserver.registerListener(this);
    myChangeObserver.start();

    myConfiguration = FreeMarkerHelper.getConfiguration(server);
  }

  public void changeOccured(String requestor) {
    reloadConfiguration();
  }

  public void dispose() {
    myChangeObserver.stop();
  }

  public boolean isPaused() {
    return myPaused;
  }

  public String getAccount() {
    return myAccount;
  }

  public String getUser() {
    return myUser;
  }

  public String getPassword() {
    return myPassword;
  }

  public void setAccount(String account) {
    myAccount = account;
  }

  public void setUser(String user) {
    myUser = user;
  }

  public void setPassword(String password) {
    myPassword = password;
  }

  public Template getTemplate(@NotNull String name) throws IOException {
    return myConfiguration.getTemplate("/" + Constants.NOTIFICATOR_TYPE + "/" + name + ".ftl");
  }

  public void save() {
    myChangeObserver.runActionWithDisabledObserver(new Runnable() {
      public void run() {
        FileUtil.processXmlFile(myConfigFile, new FileUtil.Processor() {
          public void process(Element rootElement) {
            rootElement.setAttribute(ACCOUNT, myAccount);
            rootElement.setAttribute(USER, myUser);
            String pass = myPassword != null ? EncryptUtil.scramble(myPassword) : null;
            rootElement.setAttribute(PASSWORD, pass);
            rootElement.setAttribute(PAUSED, Boolean.toString(myPaused));
          }
        });
      }
    });
  }

  private synchronized void reloadConfiguration() {
    LOG.info("Loading configuration file: " + myConfigFile.getAbsolutePath());
    Document document = parseFile(myConfigFile);
    if(document == null) return;
    final Element rootElement = document.getRootElement();
    myAccount = rootElement.getAttributeValue(ACCOUNT);
    myUser  = rootElement.getAttributeValue(USER);
    final String passwordAttr = rootElement.getAttributeValue(PASSWORD);
    myPassword = null;
    if (passwordAttr != null) {
      try {
        myPassword = EncryptUtil.unscramble(passwordAttr);
      } catch (Throwable e) {
        myPassword = passwordAttr;
      }
    }
    final Attribute attribute = rootElement.getAttribute(PAUSED);
    if (attribute != null) {
      myPaused = true;
    }
  }

  private Document parseFile(File configFile) {
    try {
      if (configFile.isFile()) {
        return JDOMUtil.loadDocument(configFile);
      }
    } catch (JDOMException e) {
      LOG.error("Failed to parse xml configuration file: " + configFile.getAbsolutePath(), e);
    } catch (IOException e) {
      LOG.error("I/O error occurred on attempt to parse xml configuration file: " + configFile.getAbsolutePath(), e);
    }
    return null;
  }
}
