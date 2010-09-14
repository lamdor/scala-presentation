package code.comet

import net.liftweb._
import http._
import actor._

object ChatServer extends LiftActor with ListenerManager {

  private var messages = List("Welcome")

  def createUpdate = messages

  override def lowPriority = {
    case s: String => messages ::= s ; updateListeners()
  }
}

class Chat extends CometActor with CometListener {

  private var msgs: List[String] = Nil

  def registerWith = ChatServer

  override def lowPriority = {
    case m: List[String] => msgs = m; reRender(false)
  }

  def render = {
    <div>
    <ul>
    {
      msgs.reverse.map(m => <li>{m}</li>)
    }
    </ul>
    <lift:form>
    {
      SHtml.text("", s => ChatServer ! s)
    }
    <input type="submit" value="Chat"/>
    </lift:form>
    </div>
  }
}
