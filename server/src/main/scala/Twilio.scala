package dev.myclinic.scala.server

import com.twilio.jwt.accesstoken.AccessToken
import com.twilio.jwt.accesstoken.ChatGrant
import com.twilio.jwt.accesstoken.VoiceGrant

object Twilio:
  def webphoneToken: String =
    val accountSid = System.getenv("TWILIO_SID")
    val apiKey = System.getenv("TWILIO_WEBPHONE_API_SID")
    val apiSecret = System.getenv("TWILIO_WEBPHONE_API_SECRET")
    val outgoingAppSid = System.getenv("TWILIO_WEBPHONE_APP_SID")
    val identity = "webphone"
    val grant: VoiceGrant = new VoiceGrant()
    grant.setOutgoingApplicationSid(outgoingAppSid)
    grant.setIncomingAllow(true)
    val token: AccessToken = new AccessToken.Builder(
      accountSid, apiKey, apiSecret
    ).identity(identity).grant(grant).build()
    token.toJwt()





