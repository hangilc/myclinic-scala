package dev.myclinic.scala.server

import javax.net.ssl.SSLContext
import java.security.KeyStore
import java.io.InputStream
import cats.instances.try_
import java.io.FileInputStream
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import java.security.SecureRandom

object Ssl:
  def createContext(keyStorePath: String, password: String): SSLContext =
    val keystore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    var finOption: Option[InputStream] = None
    try
      val in: InputStream = new FileInputStream(keyStorePath)
      finOption = Some(in)
      keystore.load(in, password.toCharArray())
      val keyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
      keyManagerFactory.init(keystore, password.toCharArray())
      val trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm()
      )
      trustManagerFactory.init(keystore)
      val sslContext = SSLContext.getInstance("TLS")
      sslContext.init(
        keyManagerFactory.getKeyManagers(),
        trustManagerFactory.getTrustManagers(),
        new SecureRandom()
      )
      sslContext
    finally finOption.foreach(_.close())
