package io.appetizer.intellij.remotecall;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import io.appetizer.intellij.remotecall.handler.OpenFileMessageHandler;
import io.appetizer.intellij.remotecall.listener.DocumentChangeListener;
import io.appetizer.intellij.remotecall.notifier.MessageNotifier;
import io.appetizer.intellij.remotecall.notifier.SocketMessageNotifier;
import io.appetizer.intellij.remotecall.settings.RemoteCallSettings;
import io.appetizer.intellij.remotecall.filenavigator.FileNavigatorImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class RemoteCallComponent implements ApplicationComponent {
  private static final Logger log = Logger.getInstance(RemoteCallComponent.class);
  private final RemoteCallSettings mySettings;
  public final static String version = "Appetizer.io 1.1.1";

  private ServerSocket serverSocket;
  private Thread listenerThread;

  public RemoteCallComponent(RemoteCallSettings settings) {
    mySettings = settings;
  }

  public void initComponent() {
    final int port = mySettings.getPortNumber();
    final boolean allowRequestsFromLocalhostOnly = mySettings.isAllowRequestsFromLocalhostOnly();
    EditorEventMulticaster eventMulticaster = EditorFactory.getInstance().getEventMulticaster();
    eventMulticaster.addDocumentListener(new DocumentChangeListener());
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(allowRequestsFromLocalhostOnly ? "localhost" : "0.0.0.0", port));
      log.info("Listening " + port);
    }
    catch (IOException e) {
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        public void run() {
          Messages.showMessageDialog("Can't bind with " + port + " port. Appetizer plugin won't work", "Appetizer Plugin Error",
                                     Messages.getErrorIcon());
        }
      });
      return;
    }

    MessageNotifier messageNotifier = new SocketMessageNotifier(serverSocket);
    messageNotifier.addMessageHandler(new OpenFileMessageHandler(new FileNavigatorImpl()));
    listenerThread = new Thread(messageNotifier);
    listenerThread.start();
  }

  public void disposeComponent() {
    try {
      if (listenerThread != null) {
        listenerThread.interrupt();
      }
      serverSocket.close();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  public String getComponentName() {
    return "Appetizer.io";
  }

}