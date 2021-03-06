package glokka

import org.specs2.mutable._

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

class DummyActor extends Actor {
  def receive = { case _ => }
}

class OneNodeModeSpec extends Specification {
  import Registry._

  private val rand = new Random

  private val system   = ActorSystem("MyClusterSystem")
  private val registry = Registry.start(system, "test")

  // For "ask" timeout
  private implicit val timeout = Timeout(60, TimeUnit.SECONDS)

  private def randomName() = rand.nextInt.toString

  "One-node mode (local mode or cluster with only one node)" should {
    "Register result Created" in {
      val name   = randomName()
      val props  = Props[DummyActor]
      val future = registry ? Register(name, props)
      val result = Await.result(future, timeout.duration)

      result must haveClass[Created]

      val ok = result.asInstanceOf[Created]
      ok.name mustEqual name
    }

    //--------------------------------------------------------------------------

    "Lookup result Found" in {
      val name  = randomName()
      val props = Props[DummyActor]
      registry ! Register(name, props)

      val future = registry ? Lookup(name)
      val result = Await.result(future, timeout.duration)

      result must haveClass[Found]

      val ok = result.asInstanceOf[Found]
      ok.name mustEqual name
    }

    "Lookup result NotFound" in {
      val name   = randomName()
      val future = registry ? Lookup(name)
      val result = Await.result(future, timeout.duration)

      result must haveClass[NotFound]

      val ok = result.asInstanceOf[NotFound]
      ok.name mustEqual name
    }
  }
}
