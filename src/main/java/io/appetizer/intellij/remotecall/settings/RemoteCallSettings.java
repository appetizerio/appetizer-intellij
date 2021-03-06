package io.appetizer.intellij.remotecall.settings;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(name = "AppetizerRemoteCall", storages = {@Storage(file = StoragePathMacros.APP_CONFIG + "/appetizerremotecall.xml")})
public class RemoteCallSettings implements PersistentStateComponent<RemoteCallSettings> {
  private int myPortNumber = 8097;
  private boolean myAllowRequestsFromLocalhostOnly = true;

  public static RemoteCallSettings getInstance() {
    return ServiceManager.getService(RemoteCallSettings.class);
  }

  public int getPortNumber() {
    return myPortNumber;
  }

  public void setPortNumber(int portNumber) {
    myPortNumber = portNumber;
  }

  public boolean isAllowRequestsFromLocalhostOnly() {
    return myAllowRequestsFromLocalhostOnly;
  }

  public void setAllowRequestsFromLocalhostOnly(boolean allowRequestsFromLocalhostOnly) {
    myAllowRequestsFromLocalhostOnly = allowRequestsFromLocalhostOnly;
  }

  @Nullable
  @Override
  public RemoteCallSettings getState() {
    return this;
  }

  @Override
  public void loadState(RemoteCallSettings remoteCallSettings) {
    XmlSerializerUtil.copyBean(remoteCallSettings, this);
  }
}