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
import jetbrains.buildServer.serverSide.ServerPaths;
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
  private static final String CONFIG_FILENAME = "vso-rooms-notifier-config.xml";

  private static final String PAUSED = "paused";

  private final File myConfigFile;
  private final FileWatcher myChangeObserver;
  private final Configuration myConfiguration;

  private boolean myPaused;

  public VSONotificatorConfig(@NotNull ServerPaths serverPaths) throws IOException {
    final File configDir = new File(serverPaths.getConfigDir(), FreeMarkerHelper.TEMPLATES_ROOT + "/" + Constants.NOTIFICATOR_TYPE);
    configDir.mkdirs();
    myConfigFile = new File(configDir, CONFIG_FILENAME);
    FileUtil.copyResourceIfNotExists(getClass(), "/message_templates/" + CONFIG_FILENAME, myConfigFile);
    reloadConfiguration();

    copyMessageTemplates(configDir);

    myChangeObserver = new FileWatcher(myConfigFile);
    myChangeObserver.setSleepingPeriod(10000);
    myChangeObserver.registerListener(this);
    myChangeObserver.start();

    myConfiguration = FreeMarkerHelper.getConfiguration(serverPaths);
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

  public void setPaused(boolean paused) {
    myPaused = paused;
  }

  public Template getTemplate(@NotNull String name) throws IOException {
    return myConfiguration.getTemplate("/" + Constants.NOTIFICATOR_TYPE + "/" + name + ".ftl");
  }

  public void save() {
    myChangeObserver.runActionWithDisabledObserver(new Runnable() {
      public void run() {
        FileUtil.processXmlFile(myConfigFile, new FileUtil.Processor() {
          public void process(Element rootElement) {
            rootElement.setAttribute(PAUSED, Boolean.toString(myPaused));
          }
        });
      }
    });
  }

  private void copyMessageTemplates(File targetDirectory) {
    copyResourceWithDist("common.ftl", targetDirectory);
    copyResourceWithDist("responsibility.ftl", targetDirectory);
    copyResourceWithDist("build_failed.ftl", targetDirectory);
    copyResourceWithDist("build_failed_to_start.ftl", targetDirectory);
    copyResourceWithDist("build_failing.ftl", targetDirectory);
    copyResourceWithDist("build_probably_hanging.ftl", targetDirectory);
    copyResourceWithDist("build_started.ftl", targetDirectory);
    copyResourceWithDist("build_successful.ftl", targetDirectory);
    copyResourceWithDist("labeling_failed.ftl", targetDirectory);
    copyResourceWithDist("build_type_responsibility_assigned.ftl", targetDirectory);
    copyResourceWithDist("build_type_responsibility_changed.ftl", targetDirectory);
    copyResourceWithDist("test_responsibility_assigned.ftl", targetDirectory);
    copyResourceWithDist("test_responsibility_changed.ftl", targetDirectory);
    copyResourceWithDist("multiple_test_responsibility_assigned.ftl", targetDirectory);
    copyResourceWithDist("multiple_test_responsibility_changed.ftl", targetDirectory);
    copyResourceWithDist("mute.ftl", targetDirectory);
    copyResourceWithDist("tests_muted.ftl", targetDirectory);
    copyResourceWithDist("tests_unmuted.ftl", targetDirectory);
    copyResourceWithDist("build_problem_responsibility_assigned.ftl", targetDirectory);
    copyResourceWithDist("build_problem_responsibility_changed.ftl", targetDirectory);
    copyResourceWithDist("build_problems_muted.ftl", targetDirectory);
    copyResourceWithDist("build_problems_unmuted.ftl", targetDirectory);
  }

  private void copyResourceWithDist(@NotNull String name, @NotNull File targetDirectory) {
    FileUtil.copyResourceWithDist(getClass(), "/message_templates/" + name, new File(targetDirectory, name));
  }

  private synchronized void reloadConfiguration() {
    LOG.info("Loading configuration file: " + myConfigFile.getAbsolutePath());
    Document document = parseFile(myConfigFile);
    if(document == null) return;
    final Attribute attribute = document.getRootElement().getAttribute(PAUSED);
    myPaused = attribute == null || Boolean.parseBoolean(attribute.getValue());
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
