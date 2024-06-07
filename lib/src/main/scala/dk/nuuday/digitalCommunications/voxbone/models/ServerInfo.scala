package dk.nuuday.digitalCommunications.voxbone.models

case class ServerInfo(address: String, sslEnabled: Boolean = true)

object ServerInfo {
  def withIpAndPort(ip: String, port: Int, sslEnabled: Boolean): ServerInfo = ServerInfo(s"$ip:$port",sslEnabled)
  def withDns(serverName: String): ServerInfo = ServerInfo(serverName)
}
